
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
    pub json: Box<dyn json::JsonService + Sync>
}

impl module::Module for CoreModule {

    fn init(&self) {
        println!("Core init");
    }

    fn start(&self) {
        println!("Core start")
    }

}