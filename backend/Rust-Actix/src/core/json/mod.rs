
pub trait JsonService {
    fn to_json(&self) -> String;
}

pub fn new() -> Box<dyn JsonService + Sync> {
    Box::new(JsonServiceSerde{})
}

struct JsonServiceSerde {}

impl JsonService for JsonServiceSerde {

    fn to_json(&self) -> String {
        return String::new()
    }

}