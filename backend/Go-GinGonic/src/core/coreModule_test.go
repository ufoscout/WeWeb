package core_test

import (
		"testing"
	"time"

	"github.com/stretchr/testify/assert"
		"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/testUtil"
		"net/http"
					"strconv"
			)

func Test_ServerShouldStart(t *testing.T) {

	coreModule := testUtil.StaticAppContext()

	router := coreModule.Services.Router
	path := "/coreModule/test1/call-" + strconv.Itoa(int(time.Now().Unix()))
	router.Get(path, func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte(""))
	})

	coreModule.Start()
	go coreModule.StartServer()
	defer coreModule.StopServer()

	time.Sleep(100 * time.Millisecond)

	assert.True(t, coreModule.ServerPort() > 0)

	resp, err := http.Get("http://127.0.0.1:" + strconv.Itoa(coreModule.ServerPort()) + path)
	defer resp.Body.Close()
	assert.Nil(t, err)
	assert.NotNil(t, resp)
	assert.Equal(t, http.StatusOK, resp.StatusCode)
}

func Test_ShouldReplyToHttpCall(t *testing.T) {

	router := testUtil.StaticAppContext().Services.Router
	path := "/coreModule/test2/call-" + strconv.Itoa(int(time.Now().Unix()))
	router.Get(path, func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte(""))
	})

	response := testUtil.PerformRequest("GET", path, nil)
	assert.Equal(t, http.StatusOK, response.Code)

	response = testUtil.PerformRequest("GET", path, nil)
	assert.Equal(t, http.StatusOK, response.Code)

	response = testUtil.PerformRequest("GET", path, nil)
	assert.Equal(t, http.StatusOK, response.Code)
}
