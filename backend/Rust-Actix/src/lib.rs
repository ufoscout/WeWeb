extern crate config;

#[macro_use] extern crate serde_derive;
#[macro_use] extern crate log;
#[macro_use] extern crate failure;
#[macro_use] extern crate failure_derive;
extern crate fern;
extern crate chrono;

pub mod auth;
pub mod json;
pub mod jwt;
pub mod logger;
pub mod module;
pub mod module_core;
pub mod module_actix_web;

pub struct App {
    pub actix_web: module_actix_web::ActixWebModule,
    pub core: module_core::CoreModule
}

pub fn start() -> Result<App, failure::Error> {
    println!("Startup - Start application setup... ");

    let mut settings = config::Config::default();
    settings
        .merge(config::File::with_name("./config/Config")).unwrap()
        .merge(config::Environment::new().separator("__")).unwrap();


    println!("Startup - Load configuration... ");

    let conf = module_core::config::new(settings.clone());

    println!("Startup - Setup logger... ");

    logger::setup_logger(&conf.logger).unwrap();

    let core = module_core::new(conf);
    let actix_web = module_actix_web::new(module_actix_web::config::new(settings.clone()));
    {
        let modules: Vec<&dyn module::Module> = vec![&core, &actix_web];
        module::start(&modules);
    }

    return Ok(App {
        actix_web,
        core
    });
}

#[cfg(test)]
#[macro_use]
extern crate lazy_static;

#[cfg(test)]
pub mod test_root {

    use std::env;
    extern crate actix_web;

    use self::actix_web::{test};
    use module_actix_web::server::Server;

    lazy_static! {
        pub static ref IT_CONTEXT: super::App = start_it_context();
    }

    fn start_it_context() -> super::App {
        env::set_var("CORE__SERVER__PORT", "0");
        return super::start().unwrap();
    }

    pub fn new_test_server(server: &Server) -> test::TestServer {
        let clone = server.routers.clone();
        test::TestServer::with_factory(move || {
            let mut apps = vec![];
            for router in clone.lock().unwrap().iter() {
                apps.push(router.configure());
            }
            apps
        })
    }
}