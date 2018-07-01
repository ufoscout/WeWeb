package testUtil

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"os"
	"path"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
		"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/starter"
	"net/http"
	"net/http/httptest"
	"io"
	"encoding/json"
	"bytes"
)

var initialized = false
var context *core.Module

func StaticAppContext() *core.Module {
	if initialized==false {
		os.Setenv("SERVER_PORT", "0")
		context = starter.StartApp(path.Join(MainFolderPath(), config.CONFIG_FILE_NAME))
		initialized=true
		//go context.StartServer()
		//time.Sleep(25 * time.Millisecond)
	}
	return context
}

func PerformRequest(method string, path string, body interface{}) *httptest.ResponseRecorder {
	return PerformRequestWithHandler(StaticAppContext().Services.Router, method, path, body)
}

func PerformRequestWithHandler(r http.Handler, method string, path string, body interface{}) *httptest.ResponseRecorder {
	var reqBody io.Reader
	if (body!=nil) {
		jsonReq, err := json.Marshal(body)
		if err!=nil {
			panic(err)
		}
		reqBody = bytes.NewReader(jsonReq)
	}
	req, _ := http.NewRequest(method, path, reqBody)
	w := httptest.NewRecorder()
	r.ServeHTTP(w, req)
	return w
}
