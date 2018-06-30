package starter

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/module"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/auth"
)

func StartApp(configPath string, modules ...module.Module) *core.Module {
	config := config.Load(configPath)

	authModule := auth.New(&config)
	coreModule := core.New(&config)

	allModules := append([]module.Module{
		coreModule,
		authModule,
	}, modules...)

	module.Go(
		allModules...,
	)
	return coreModule
}
