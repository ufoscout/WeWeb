extern crate config as rs_config;

#[derive(Debug)]
pub struct ServerConfig {
    pub port: u32,
    pub shutdown_timeout: u32
}

pub fn new(conf: rs_config::Config) -> ServerConfig {
    ServerConfig{
        port: conf.get_int("core.server.port").unwrap() as u32,
        shutdown_timeout: conf.get_int("core.server.shutdown.timeout").unwrap_or(60) as u32
    }
}