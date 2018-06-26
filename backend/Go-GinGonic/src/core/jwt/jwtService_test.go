package jwt_test

import (
	"testing"
		"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/testUtil"
)

func Test(t *testing.T) {

	context := testUtil.StaticAppContext()

	jwt := context.Services.Jwt

	jwt.Example()

}
