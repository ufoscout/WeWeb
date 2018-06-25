package jwt_test

import (
	"testing"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/starter"
)

func Test(t *testing.T) {

	context := starter.StaticAppContext()

	jwt := context.Services.Jwt

	jwt.Example()

}
