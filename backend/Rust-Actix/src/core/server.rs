extern crate actix;
extern crate actix_web;

use core;
use std::sync::{Arc, Mutex};

pub trait Router: Send + Sync {
    fn configure(&self) -> actix_web::App;
}

pub struct Server {
    pub port: u32,
    routers: Arc<Mutex<Vec<Box<Router>>>>,
}

impl Server {
    pub fn new(config: &core::config::ServerConfig) -> Server {
        Server {
            port: config.port,
            routers: Arc::new(Mutex::new(vec![])),
        }
    }

    // Modules are registered calling this method
    pub fn register(&self, router: Box<Router>) {
        self.routers.lock().unwrap().push(router);
    }

    // It dynamically registers the modules and starts the server
    pub fn start(&self) {
        let address = format!("0.0.0.0:{}", self.port);
        info!("Starting server at {}...", &address);
        let clone = self.routers.clone();
        actix_web::server::new(move || {
            let mut apps = vec![];
            for router in clone.lock().unwrap().iter() {
                apps.push(router.configure());
            }
            apps
        })
        .bind(&address).expect({
            error!("Can not start server at [{}]", &address);
            &format!("Can not start server at [{}]", &address)
        })
        .shutdown_timeout(60)    // <- Set shutdown timeout to 60 seconds
        .run();
    }
}
