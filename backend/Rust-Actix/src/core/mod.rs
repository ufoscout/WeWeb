
pub mod config;
pub mod json;
pub mod jwt;
pub mod server;

use super::module;

pub fn new(config: config::CoreConfig) -> CoreModule {

    let jwt = jwt::new(&config.jwt);
    let server = server::Server::new(&config.server);

    CoreModule{
        config,
        json: json::new(),
        jwt,
        server
    }
}

pub struct CoreModule {
    pub config: config::CoreConfig,
    pub json: json::JsonService,
    pub jwt: jwt::JwtService,
    pub server: server::Server
}

impl module::Module for CoreModule {

    fn init(&self) {
        info!("Core init");
    }

    fn start(&self) {
        info!("Core start")
    }

}

impl CoreModule {
    pub fn start_server(&self) {
        info!("CoreModule: Start server");
        self.server.start()
    }
}