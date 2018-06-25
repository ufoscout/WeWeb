package jwt

import (
	"testing"
	config2 "github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/stretchr/testify/assert"
)

func Test(t *testing.T) {

	config := config2.JwtConfig{
		Secret: "mySecret",
		SignatureAlgorithm: "HS512",
		TokenValidityMinutes: 10,
	}
	jwt, err := New(config)
	assert.Nil(t, err)
	
	jwt.Example()

}
