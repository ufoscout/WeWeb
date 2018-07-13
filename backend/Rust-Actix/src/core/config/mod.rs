extern crate config as rs_config;

#[derive(Debug)]
pub struct CoreConfig {
    pub logger: LoggerConfig,
    pub jwt: JwtConfig,
    pub server: ServerConfig
}

#[derive(Debug)]
pub struct LoggerConfig {
    pub root_level: String,
    pub level: String,
    pub output_system_enable: bool,
    pub output_file_enable: bool,
    pub output_file_name: String
}

#[derive(Debug)]
pub struct JwtConfig {
    pub secret: String,
    pub signature_algorithm: String,
    pub token_validity_minutes: u32
}

#[derive(Debug)]
pub struct ServerConfig {
    pub port: u32
}

pub fn new(conf: rs_config::Config) -> CoreConfig {
    CoreConfig {
        logger: LoggerConfig {
            root_level: conf.get_str("logger.root_level").unwrap(),
            level: conf.get_str("logger.level").unwrap(),
            output_system_enable: conf.get_bool("logger.output_system_enable").unwrap(),
            output_file_enable: conf.get_bool("logger.output_file_enable").unwrap(),
            output_file_name: conf.get_str("logger.output_file_name").unwrap()
        },
        jwt: JwtConfig{
            secret: conf.get_str("jwt.secret").unwrap(),
            signature_algorithm: conf.get_str("jwt.signatureAlgorithm").unwrap(),
            token_validity_minutes: conf.get_int("jwt.tokenValidityMinutes").unwrap() as u32
        },
        server: ServerConfig{
            port: conf.get_int("server.port").unwrap() as u32
        }
    }
}

#[cfg(test)]
mod test {

    use test_root::IT_CONTEXT;

    #[test]
    fn should_read_config_and_use_env_vars() {

        let app = &IT_CONTEXT;
        assert_eq!(0, app.core.config.server.port)
    }

}