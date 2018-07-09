package jwt

import (
	"time"
	"github.com/dgrijalva/jwt-go"
	"errors"
	"github.com/ufoscout/WeWeb/backend/Go-Chi/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-Chi/src/core/json"
	"fmt"
)

type JwtService struct {
	secret               *[]byte
	signingMethod        *jwt.SigningMethodHMAC
	tokenValidityMinutes int
	jsonService          *json.JsonService
}

func New(config config.JwtConfig, jsonService *json.JsonService) (*JwtService, error) {

	signingMethod, err := SigningMethod(config.SignatureAlgorithm)
	if err!=nil {
		return nil, err
	}

	secret := []byte(config.Secret)

	return &JwtService{
		secret: &secret,
		signingMethod: signingMethod,
		tokenValidityMinutes: config.TokenValidityMinutes,
		jsonService: jsonService,
	}, nil
}

func SigningMethod(method string) (*jwt.SigningMethodHMAC, error) {
	switch method {
		case "HS512": return jwt.SigningMethodHS512, nil
		case "HS256": return jwt.SigningMethodHS256, nil
		default: return nil, errors.New("Not supported JWT signing method: " + method)
	}
}

type TokenClaims struct {
	Payload        string `json:"payload"`
	jwt.StandardClaims
}

func (jwtService *JwtService) Generate(payload interface{}) (string, error) {
	return jwtService.GenerateWithSubject("", payload)
}

func (jwtService *JwtService) GenerateWithSubject(subject string, payload interface{}) (string, error) {
    issuedAt := time.Now()
	expireDate := issuedAt.Add(time.Minute * time.Duration(jwtService.tokenValidityMinutes))
	return jwtService.GenerateWithSubjectAndDates(subject, payload, issuedAt, expireDate)
}

func (jwtService *JwtService) GenerateWithSubjectAndDates(
	subject string, payload interface{}, issuedDate time.Time, expireDate time.Time) (string, error) {

	jsonPayload, err := jwtService.jsonService.ToJson(payload)
	if err!=nil {
		return "", err
	}

	claims := TokenClaims{
		Payload:        string(jsonPayload),
		StandardClaims: jwt.StandardClaims{
			Subject: subject,
			IssuedAt: issuedDate.Unix(),
			ExpiresAt: expireDate.Unix(),
		},
	}

	token := jwt.NewWithClaims(jwtService.signingMethod, claims)

	// Sign and get the complete encoded token as a string using the secret
	return token.SignedString(*jwtService.secret)
}

func (jwtService *JwtService) Parse(tokenString string, v interface{}) error {

	token, err := jwtService.GetDecodedToken(tokenString)

	if (err==nil && token.Valid()==nil) {
		//fmt.Println("Token Payload is:")
		//fmt.Println(token.Payload)
		err = jwtService.jsonService.FromJson([]byte(token.Payload), v)
		if err!=nil {
			return err
		}
		return nil
	} else {
		//fmt.Println("Error")
		//fmt.Println(err)
		return err
	}
}

func (jwtService *JwtService) GetDecodedToken(tokenString string) (*TokenClaims, error) {

	var tokenClaims TokenClaims
	_, err := jwt.ParseWithClaims(tokenString, &tokenClaims, func(token *jwt.Token) (interface{}, error) {
		// Don't forget to validate the alg is what you expect:
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("Unexpected signing method: %v", token.Header["alg"])
		}

		// hmacSampleSecret is a []byte containing your secret, e.g. []byte("my_secret_key")
		return *jwtService.secret, nil
	})

	//fmt.Println("------------------")
	//fmt.Println(tokenClaims)
	//fmt.Println(tokenClaims.IssuedAt)
	//fmt.Println(tokenClaims.ExpiresAt)
	//fmt.Println(tokenClaims.Valid())
	//fmt.Println("------------------")

	return &tokenClaims, err

}
