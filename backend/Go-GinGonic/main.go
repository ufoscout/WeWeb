package main

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/configuration"
)

func main() {

	config := configuration.Load(configuration.CONFIG_FILE_NAME)

	coreModule := core.NewModule(&config)

	coreModule.Start()

}
