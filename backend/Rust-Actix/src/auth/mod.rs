
use super::module;

mod model;
mod service;

pub fn new() -> AuthModule {
    AuthModule{}
}

pub struct AuthModule {
}

impl module::Module for AuthModule {

    fn init(&self) {
        info!("Auth init");
    }

    fn start(&self) {
        info!("Auth start")
    }

}