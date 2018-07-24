pub mod config;

use super::json;
use super::jwt;
use super::module;

pub fn new(config: config::CoreConfig) -> CoreModule {

    println!("Creating CoreModule with configuration:\n{:#?}", config);
    info!("Creating CoreModule with configuration:\n{:#?}", config);

    let jwt = jwt::new(&config.jwt);

    CoreModule{
        config,
        json: json::new(),
        jwt
    }
}

pub struct CoreModule {
    pub config: config::CoreConfig,
    pub json: json::JsonService,
    pub jwt: jwt::JwtService,
}

impl module::Module for CoreModule {

    fn init(&self) {
        info!("Core init");
    }

    fn start(&self) {
        info!("Core start")
    }

}