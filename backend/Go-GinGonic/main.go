package main

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/starter"
)

func main() {
	coreModule := starter.StartApp(config.CONFIG_FILE_NAME)
    coreModule.StartServer()
}
