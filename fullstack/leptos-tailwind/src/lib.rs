pub mod app;
pub mod error_template;
pub mod server_fn;
#[cfg(feature = "ssr")]
pub mod ssr;

#[cfg(feature = "hydrate")]
#[wasm_bindgen::prelude::wasm_bindgen]
pub fn hydrate() {
    use crate::app::*;
    console_error_panic_hook::set_once();
    leptos::mount_to_body(App);
}
