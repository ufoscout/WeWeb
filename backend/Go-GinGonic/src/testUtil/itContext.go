package testUtil

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"os"
	"path"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"time"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/starter"
)

var initialized = false
var context *core.Module

func StaticAppContext() *core.Module {
	if initialized==false {
		os.Setenv("SERVER_PORT", "0")
		context = starter.StartApp(path.Join(MainFolderPath(), config.CONFIG_FILE_NAME))
		initialized=true
		go context.StartServer()
		time.Sleep(25 * time.Millisecond)
	}
	return context
}
