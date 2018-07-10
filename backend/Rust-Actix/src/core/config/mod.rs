extern crate config as rs_config;

#[derive(Debug)]
pub struct ServerConfig {
    port: u32
}

#[derive(Debug)]
pub struct JwtConfig {
    secret: String,
    signature_algorithm: String,
    token_validity_minutes: u32
}

#[derive(Debug)]
pub struct CoreConfig {
    server: ServerConfig,
    jwt: JwtConfig,
}

pub fn new(conf: rs_config::Config) -> CoreConfig {
    CoreConfig {
        server: ServerConfig{
            port: conf.get_int("server.port").unwrap() as u32
        },
        jwt: JwtConfig{
            secret: conf.get_str("jwt.secret").unwrap(),
            signature_algorithm: conf.get_str("jwt.signatureAlgorithm").unwrap(),
            token_validity_minutes: conf.get_int("jwt.tokenValidityMinutes").unwrap() as u32
        }
    }
}

#[cfg(test)]
mod test {

    use super::super::super::test::IT_CONTEXT;

    #[test]
    fn should_init_all_then_start_all() {

        let app = &IT_CONTEXT;
        assert_eq!(0, app.core.config.server.port)
    }

}