extern crate config;

mod core;
mod module;

pub struct App {
    core: core::CoreModule
}

pub fn start() -> App {

    let mut settings = config::Config::default();
    settings
        .merge(config::File::with_name("./config/Config")).unwrap()
        .merge(config::Environment::with_prefix("APP")).unwrap();


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
pub mod test {
    use std::env;

    lazy_static! {
        pub static ref IT_CONTEXT: super::App = start_it_context();
    }

    fn start_it_context() -> super::App {
        env::set_var("APP_SERVER.PORT", "0");
        return super::start();
    }
}