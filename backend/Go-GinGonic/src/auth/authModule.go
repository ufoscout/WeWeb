package auth

import (
									"github.com/ufoscout/WeWeb/backend/Go-GinGonic/src/core/config"
)

type Service struct {
}

type Module struct {
	Services   Service
}

func New(config *config.Config) *Module {
	module := Module{}

	module.Services = Service{}

	return &module
}

func (core *Module) Init() error {
	return nil
}

func (core *Module) Start() error {
	return nil
}
