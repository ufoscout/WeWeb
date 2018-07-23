extern crate rust_actix;

fn main() {
    let app = rust_actix::start().unwrap();
    //app.core.server.configure(rust_actix::core::server::config_one);
    //app.core.server.configure(rust_actix::core::server::config_two);
    app.core.start_server()
}