package main

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/module"
)

func main() {
	StartApp()
}

func StartApp() {
	config := config.Load(config.CONFIG_FILE_NAME)

	coreModule := core.New(&config)

	module.Go(
		coreModule,
	)

	coreModule.StartServer()

}
