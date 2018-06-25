package starter

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/module"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"os"
	"path"
	"time"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/util"
)

func StartApp(configPath string, modules ...module.Module) *core.Module {
	config := config.Load(configPath)

	coreModule := core.New(&config)

	allModules := append([]module.Module{coreModule}, modules...)

	module.Go(
		allModules...,
	)
	return coreModule
}


var initialized = false
var context *core.Module

func StaticAppContext() *core.Module {
	if initialized==false {
		os.Setenv("SERVER_PORT", "0")
		context = StartApp(path.Join(util.MainFolderPath(), config.CONFIG_FILE_NAME))
		initialized=true
		go context.StartServer()
		time.Sleep(25 * time.Millisecond)
	}
	return context
}