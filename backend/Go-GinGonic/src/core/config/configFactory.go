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
			Port: up.GetString("server.port")},
		Frontend: FrontendConfig{
			Enabled: up.GetBool("server.resources.static.enabled"),
			ResourcesPath: up.GetString("server.resources.static.path"),
		},
		Jwt: JwtConfig{
			Secret: up.GetString("jwt.secret"),
			SignatureAlgorithm: up.GetString("jwt.signatureAlgorithm"),
			TokenValidityMinutes: up.GetInt("jwt.tokenValidityMinutes"),
		},
	}

	return config
}
