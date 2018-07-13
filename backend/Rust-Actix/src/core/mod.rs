
pub mod config;
pub mod json;
use super::module;

pub fn new(config: config::CoreConfig) -> CoreModule {
    CoreModule{
        config,
        json: json::new()
    }
}

pub struct CoreModule {
    pub config: config::CoreConfig,
    pub json: json::JsonService
}

impl module::Module for CoreModule {

    fn init(&self) {
        info!("Core init");
    }

    fn start(&self) {
        info!("Core start")
    }

}