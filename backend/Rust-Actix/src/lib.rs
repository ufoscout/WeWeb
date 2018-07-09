extern crate config;

mod core;
mod module;

pub fn start() {

    let mut settings = config::Config::default();
    settings
        // Add in `./Settings.toml`
        .merge(config::File::with_name("./config/Config")).unwrap()
        // Add in settings from the environment (with a prefix of APP)
        // Eg.. `APP_DEBUG=1 ./target/app` would set the `debug` key
        .merge(config::Environment::with_prefix("APP")).unwrap();


    let conf = core::config::new(settings);
    println!("{:?}", conf);
    let core_module = core::new(conf);
    println!("{:?}", core_module.config);

    module::start(&[core_module]);
}