package jwt

import (
	"time"
		"github.com/dgrijalva/jwt-go"
	"errors"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/json"
	"fmt"
)

var PAYLOAD_CLAIM_KEY = "payload"

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

type tokenClaims struct {
	Payload        string `json:"payload"`
	jwt.StandardClaims
}

func (jwtService *JwtService) Generate(payload interface{}) (string, error) {
	return jwtService.GenerateWithSubject("", payload)
}

func (jwtService *JwtService) GenerateWithSubject(subject string, payload interface{}) (string, error) {
    issuedAt := time.Now()
	expireDate := time.Now().Add(time.Minute * time.Duration(jwtService.tokenValidityMinutes))
	return jwtService.GenerateWithSubjectAndDates(subject, payload, issuedAt, expireDate)
}

func (jwtService *JwtService) GenerateWithSubjectAndDates(
	subject string, payload interface{}, issuedDate time.Time, expireDate time.Time) (string, error) {

	jsonPayload, err := jwtService.jsonService.ToJson(payload)
	if err!=nil {
		return "", err
	}

	claims := tokenClaims{
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

	// Parse takes the token string and a function for looking up the key. The latter is especially
	// useful if you use multiple keys for your application.  The standard is to use 'kid' in the
	// head of the token to identify which key to use, but the parsed token (head and claims) is provided
	// to the callback, providing flexibility.
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		// Don't forget to validate the alg is what you expect:
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("Unexpected signing method: %v", token.Header["alg"])
		}

		// hmacSampleSecret is a []byte containing your secret, e.g. []byte("my_secret_key")
		return *jwtService.secret, nil
	})

	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		payload := claims["payload"].(string)
		err = jwtService.jsonService.FromJson([]byte(payload), v)
		if err!=nil {
			return err
		}
		return nil
	} else {
		return err
	}
}
