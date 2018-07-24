extern crate actix;
extern crate actix_web;

use super::config;
use std::sync::{Arc, Mutex};

pub trait Router: Send + Sync {
    fn configure(&self) -> actix_web::App;
}

pub struct Server {
    pub port: u32,
    pub routers: Arc<Mutex<Vec<Box<Router>>>>,
}

impl Server {
    pub fn new(config: &config::ServerConfig) -> Server {
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

#[cfg(test)]
mod server_test {

    extern crate actix_web;

    use self::actix_web::{http};
    use super::{Server, Router};
    use test_root;

    #[test]
    fn should_start_the_test_server_registering_all_modules() {
        let config = super::config::ServerConfig { port: 0 };
        let server = Server::new(&config);

        server.register(Box::new(TestRouter{name: "one"}));
        server.register(Box::new(TestRouter{name: "two"}));

        let mut test_server = test_root::new_test_server(&server);

        let request_one = test_server.client(http::Method::GET, "/one/hello").finish().unwrap();
        let response_one = test_server.execute(request_one.send()).unwrap();
        assert!(response_one.status().is_success());

        let request_two = test_server.client(http::Method::GET, "/two/hello").finish().unwrap();
        let response_two = test_server.execute(request_two.send()).unwrap();
        assert!(response_two.status().is_success());

        let request_three = test_server.client(http::Method::GET, "/three/hello").finish().unwrap();
        let response_three = test_server.execute(request_three.send()).unwrap();
        assert!(!response_three.status().is_success());
    }

    struct TestRouter{
        name: &'static str
    }

    impl Router for TestRouter{
        fn configure(&self) -> actix_web::App {
            let name = self.name;
            let mut path = String::from("/");
            path.push_str(self.name);
            let path = path.as_str();
            actix_web::App::new().prefix(path).resource("/hello", move |r| r.f(move |_| format!("hello from {}", name)))
        }
    }

}
