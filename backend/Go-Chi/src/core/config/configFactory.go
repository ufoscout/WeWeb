package config

import (
	"fmt"
	"github.com/ufoscout/go-up"
)

/*
Load the config from the path folder
*/
func Load(configFile string) Config {

	up, err := go_up.NewGoUp().
		AddFile(configFile, false).
		AddReader(go_up.NewEnvReader("", false, false)). // Loading environment variables
		AddReader(go_up.NewEnvReader("", true, true)).
		Build()

	if err != nil {
		panic(fmt.Errorf("Fatal error config file: %s", err))
	}

	var config = Config{
		Server: ServerConfig{
			Port: up.GetString("core.server.port")},
		Frontend: FrontendConfig{
			Enabled: up.GetBool("core.server.resources.static.enabled"),
			ResourcesPath: up.GetString("core.server.resources.static.path"),
		},
		Jwt: JwtConfig{
			Secret: up.GetString("core.jwt.secret"),
			SignatureAlgorithm: up.GetString("core.jwt.signatureAlgorithm"),
			TokenValidityMinutes: up.GetInt("core.jwt.tokenValidityMinutes"),
		},
	}

	return config
}
