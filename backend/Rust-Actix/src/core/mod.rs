
pub mod config;
use super::module;

pub fn new(config: config::CoreConfig) -> CoreModule {
    CoreModule{
        config: config
    }
}

pub struct CoreModule {
    pub config: config::CoreConfig
}

impl module::Module for CoreModule {

    fn init(&self) {
        println!("Core init");
    }

    fn start(&self) {
        println!("Core start")
    }

}