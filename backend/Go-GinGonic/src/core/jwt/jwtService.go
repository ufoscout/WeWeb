package jwt

import (
	"time"
	"fmt"
	jwt "github.com/dgrijalva/jwt-go"
	"errors"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
)

type JwtService struct {
	secret        *[]byte
	signingMethod *jwt.SigningMethodHMAC

}

func New(config config.JwtConfig) (*JwtService, error) {

	signingMethod, err := signingMethod(config.SignatureAlgorithm)
	if err!=nil {
		return nil, err
	}

	secret := []byte(config.Secret)

	return &JwtService{
		secret: &secret,
		signingMethod: signingMethod,
	}, nil
}

func signingMethod(method string) (*jwt.SigningMethodHMAC, error) {
	switch method {
		case "HS512": return jwt.SigningMethodHS512, nil
		case "HS256": return jwt.SigningMethodHS256, nil
		default: return nil, errors.New("Not supported JWT signing method: " + method)
	}
}

func (jwtService *JwtService) Example() {
	token := jwt.NewWithClaims(jwtService.signingMethod, jwt.MapClaims{
		"foo": "bar",
		"nbf": time.Date(2015, 10, 10, 12, 0, 0, 0, time.UTC).Unix(),
	})

	// Sign and get the complete encoded token as a string using the secret
	tokenString, err := token.SignedString(*jwtService.secret)

	fmt.Println(tokenString, err)
}
