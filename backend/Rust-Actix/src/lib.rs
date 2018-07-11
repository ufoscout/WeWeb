extern crate config;

#[macro_use]
extern crate serde_derive;

mod core;
mod module;

pub struct App {
    core: core::CoreModule
}

pub fn start() -> App {

    let mut settings = config::Config::default();
    settings
        .merge(config::File::with_name("./config/Config")).unwrap()
        .merge(config::Environment::new().separator("__")).unwrap();


    let conf = core::config::new(settings);
    println!("{:?}", conf);

    let app = App {
        core: core::new(conf)
    };

    module::start(&[&app.core]);

    return app;
}

#[cfg(test)]
#[macro_use]
extern crate lazy_static;

#[cfg(test)]
pub mod test_root {

    use std::env;

    lazy_static! {
        pub static ref IT_CONTEXT: super::App = start_it_context();
    }

    fn start_it_context() -> super::App {
        env::set_var("SERVER__PORT", "0");
        return super::start();
    }
}