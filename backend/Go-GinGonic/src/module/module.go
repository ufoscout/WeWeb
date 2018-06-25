package module

type Module interface {
	Init() error
	Start() error
}

func Go(modules ...Module) {
	for _, module := range modules {
		err := module.Init()
		if err != nil {
			panic(err)
		}
	}
	for _, module := range modules {
		err := module.Start()
		if err != nil {
			panic(err)
		}
	}
}
