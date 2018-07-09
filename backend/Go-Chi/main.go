package main

import (
	"github.com/ufoscout/WeWeb/backend/Go-Chi/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-Chi/src/starter"
)

func main() {
	coreModule := starter.StartApp(config.CONFIG_FILE_NAME)
    coreModule.StartServer()
}
