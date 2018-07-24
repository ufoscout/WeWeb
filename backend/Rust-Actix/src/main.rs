extern crate rust_actix;

fn main() {
    let app = rust_actix::start().unwrap();
    //app.actix_web.server.configure(rust_actix::core::server::config_one);
    //app.actix_web.server.configure(rust_actix::core::server::config_two);
    app.actix_web.start_server()
}