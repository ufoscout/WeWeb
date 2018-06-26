package jwt_test

import (
	"testing"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/testUtil"
	"github.com/stretchr/testify/assert"
	"fmt"
	"time"
	"math/rand"
	"strconv"
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

func Test_ShouldGenerateAndParseCustomBeans(t *testing.T) {
	context := testUtil.StaticAppContext()
	jwtService := context.Services.Jwt

	message := simpleMailMessage{}
	message.From = "from-" + string(rand.Int())
	message.Subject = "sub-" + string(rand.Int())
	message.SentDate = time.Now()

	jwtString, err := jwtService.Generate(message)
	assert.Nil(t, err)
	fmt.Println("Generated JWT:\n" + jwtString)

	token, err := jwtService.GetDecodedToken(jwtString)
	assert.Nil(t, err)

	fmt.Println("Parsed JWT:\n" + token.Payload);
	assert.NotNil(t, token.Payload);
	var parsedMessage simpleMailMessage
	err = jwtService.Parse(jwtString, &parsedMessage)
	assert.NotNil(t, parsedMessage);
	assert.Equal(t, message.From, parsedMessage.From);
	assert.Equal(t, message.Subject, parsedMessage.Subject);
	assert.Equal(t, message.SentDate.Unix(), parsedMessage.SentDate.Unix());
}

func Test_ShouldSetTheExpirationDate(t *testing.T) {
	context := testUtil.StaticAppContext()
	jwtService := context.Services.Jwt

	message := simpleMailMessage{}
	message.From = "from-" + strconv.Itoa(rand.Int())
	message.Subject = "sub-" + strconv.Itoa(rand.Int())
	message.SentDate = time.Now()

	beforeTime := time.Now().Add(time.Second * time.Duration(-1000))
	jwtString, err := jwtService.Generate(message)
	assert.Nil(t, err)
	fmt.Println("Generated JWT:\n" + jwtString)

	afterTime := time.Now().Add(time.Second * time.Duration(1000))

	token, err := jwtService.GetDecodedToken(jwtString)
	assert.Nil(t, err)

	assert.True(t, token.IssuedAt >= beforeTime.Unix());
	assert.True(t, token.IssuedAt <= afterTime.Unix());

	tokenValidityMinutes := context.Services.Config.Jwt.TokenValidityMinutes
	expireTime := token.ExpiresAt
	assert.Equal(t, token.IssuedAt + int64((time.Duration(tokenValidityMinutes) * time.Minute).Seconds()), expireTime );
}

/*
func Test_ShouldFailParsingTamperedJwt(t *testing.T) {
assertThrows(SignatureException.class,
()->{

		context := testUtil.StaticAppContext()
		jwtService := context.Services.Jwt
		
message := simpleMailMessage{}
		message.From = "from-" + string(rand.Int())
		message.Subject = "sub-" + string(rand.Int())
		message.SentDate = time.Now()

		jwt, err := jwtService.Generate(message)
		assert.Nil(t, err)
		fmt.Println("Generated JWT:\n" + jwt)

jwtService.parse(jwt + 1, String.class);
});
}

func Test_ShouldFailParsingExpiredBeans(t *testing.T) {
	context := testUtil.StaticAppContext()
	jwtService := context.Services.Jwt
assertThrows(TokenExpiredException.class,
()->{
final SimpleMailMessage userContext = new SimpleMailMessage();
jwt := jwtService.generate("", userContext, new Date(), new Date(System.currentTimeMillis() -1 ));
jwtService.parse(JWT, SimpleMailMessage.class);
});
}

func Test_ShouldAcceptNotExpiredBeans(t *testing.T) {
	context := testUtil.StaticAppContext()
	jwtService := context.Services.Jwt
final SimpleMailMessage userContext = new SimpleMailMessage();
jwt := jwtService.generate(userContext);
assertNotNull(jwtService.parse(jwt, SimpleMailMessage.class));
}

*/
type simpleMailMessage struct {
	From     string    `json:"from"`
	SentDate time.Time `json:"sentDate"`
	Subject  string    `json:"subject"`
}