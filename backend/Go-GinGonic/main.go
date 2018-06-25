package main

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/configuration"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/module"
)

func main() {
	StartApp()
}

func StartApp() {
	config := configuration.Load(configuration.CONFIG_FILE_NAME)

	coreModule := core.New(&config)

	module.Go(
		coreModule,
	)

	coreModule.StartServer()

}
