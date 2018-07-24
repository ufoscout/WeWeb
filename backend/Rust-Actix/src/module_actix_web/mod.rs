pub mod config;
pub mod server;

use super::module;

pub fn new(config: config::ServerConfig) -> ActixWebModule {

    println!("Creating ActixWebModule with configuration:\n{:#?}", config);
    info!("Creating ActixWebModule with configuration:\n{:#?}", config);

    let server = server::Server::new(&config);
    ActixWebModule{
        config,
        server
    }
}

pub struct ActixWebModule {
    pub config: config::ServerConfig,
    pub server: server::Server
}

impl module::Module for ActixWebModule {

    fn init(&self) {
        info!("ActixWebModule init");
    }

    fn start(&self) {
        info!("ActixWebModule start")
    }

}

impl ActixWebModule {
    pub fn start_server(&self) {
        info!("ActixWebModule: Start server");
        self.server.start()
    }
}