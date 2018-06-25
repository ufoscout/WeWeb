package config

type JwtConfig struct {
	Secret string
	SignatureAlgorithm string
	TokenValidityMinutes int
}
