package starter

import (
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/module"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core"
	"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
				)

func StartApp(configPath string, modules ...module.Module) *core.Module {
	config := config.Load(configPath)

	coreModule := core.New(&config)

	allModules := append([]module.Module{
		coreModule,
	}, modules...)

	module.Go(
		allModules...,
	)
	return coreModule
}
