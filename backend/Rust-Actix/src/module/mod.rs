
pub trait Module {
    fn init(&self);
    fn start(&self);
}

pub fn start(modules: &[impl Module]) {
    for module in modules {
        module.init();
    }
    for module in modules {
        module.start();
    }
}