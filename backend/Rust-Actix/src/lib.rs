extern crate config;

#[macro_use] extern crate serde_derive;
#[macro_use] extern crate log;
extern crate fern;
extern crate chrono;

mod core;
mod module;

use std::str::FromStr;

pub struct App {
    core: core::CoreModule
}

pub fn start() -> App {

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

    let app = App {
        core: core::new(conf)
    };

    module::start(&[&app.core]);

    return app;
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

    if logger_config.output_system_enable {
        log_dispatcher = log_dispatcher.chain(std::io::stdout());
    }

    if logger_config.output_file_enable {
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
        return super::start();
    }
}