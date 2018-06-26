package jwt_test

import (
	"testing"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/testUtil"
	"github.com/stretchr/testify/assert"
	"fmt"
	"time"
)

func Test_ShouldCreateTheTokenWithSubject(t *testing.T) {

	context := testUtil.StaticAppContext()
	jwt := context.Services.Jwt

	subject := "Subject of Hello World"
	payload := "helloWorld"

	token, err := jwt.GenerateWithSubject(subject, payload)

	assert.Nil(t, err)

	fmt.Println(token)
	assert.NotNil(t, token)

}

func Test_ShouldParseTheToken(t *testing.T) {

	context := testUtil.StaticAppContext()
	jwt := context.Services.Jwt

	subject := "Subject of Hello World"
	payload := "helloWorld"

	token, _ := jwt.GenerateWithSubject(subject, payload)
	var v string
	jwt.Parse(token, &v)
	fmt.Println(v)
	assert.Equal(t, payload, v)
}

type simpleMailMessage struct {
	From     string     `json:"from"`
	SentDate time.Time  `json:"sentDate"`
	Subject  string     `json:"subject"`
}
