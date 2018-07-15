extern crate config;

#[macro_use] extern crate serde_derive;
#[macro_use] extern crate log;
#[macro_use] extern crate failure;
#[macro_use] extern crate failure_derive;
extern crate fern;
extern crate chrono;

mod auth;
mod core;
mod module;

use std::str::FromStr;

pub struct App {
    auth: auth::AuthModule,
    core: core::CoreModule
}

pub fn start() -> Result<App, failure::Error> {
    println!("Startup - Start application setup... ");

    let mut settings = config::Config::default();
    settings
        .merge(config::File::with_name("./config/Config")).unwrap()
        .merge(config::Environment::new().separator("__")).unwrap();


    println!("Startup - Load configuration... ");

    let conf = core::config::new(settings);

    println!("Startup - Setup logger... ");

    setup_logger(&conf.logger).unwrap();

    println!("Starting application with configuration:\n{:#?}", conf);
    info!("Starting application with configuration:\n{:#?}", conf);

    let core = core::new(conf);
    let auth = auth::new();

    {
        let modules: Vec<&dyn module::Module> = vec![&core, &auth];
        module::start(&modules);
    }

    return Ok(App {
        auth,
        core
    });
}

fn setup_logger(logger_config: &core::config::LoggerConfig) -> Result<(), fern::InitError> {
    let mut log_dispatcher = fern::Dispatch::new()
        .format(|out, message, record| {
            out.finish(format_args!(
                "{}[{}][{}] {}",
                chrono::Local::now().format("[%Y-%m-%d][%H:%M:%S]"),
                record.target(),
                record.level(),
                message
            ))
        })
        .level(log::LevelFilter::from_str(&logger_config.root_level).unwrap())
        .level_for("rust_actix", log::LevelFilter::from_str(&logger_config.level).unwrap());

    if logger_config.output_system_enabled {
        log_dispatcher = log_dispatcher.chain(std::io::stdout());
    }

    if logger_config.output_file_enabled {
        log_dispatcher = log_dispatcher.chain(fern::log_file(&logger_config.output_file_name)?);
    }

    log_dispatcher.apply()?;

    Ok(())
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
        return super::start().unwrap();
    }
}