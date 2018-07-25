extern crate coreutils_auth as auth_mod;
extern crate coreutils_module as module;

pub mod config;
pub mod server;
pub mod service;

mod error;

use std::sync::Arc;
use ::module_core;

pub fn new(config: config::ServerConfig, core: &module_core::CoreModule, role_provider: Box<auth_mod::RolesProvider>) -> ActixWebModule {

    println!("Creating ActixWebModule with configuration:\n{:#?}", config);
    info!("Creating ActixWebModule with configuration:\n{:#?}", config);

    let server = server::Server::new(&config);
    let auth_service = service::new(Arc::new(auth_mod::new(role_provider)), core.jwt.clone());
    ActixWebModule{
        auth: Arc::new(auth_service),
        config,
        server
    }
}

pub struct ActixWebModule {
    pub config: config::ServerConfig,
    pub auth: Arc<service::AuthContextService>,
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