extern crate actix_web;

static JWT_TOKEN_HEADER: &str = "Authorization";
static JWT_TOKEN_HEADER_SUFFIX: &str = "Bearer ";

pub struct AuthContextService {}

pub fn new() -> AuthContextService {
    AuthContextService {}
}

impl AuthContextService {

    pub fn token_from(&self, req: &actix_web::HttpRequest) -> String {
        let result: Option<&actix_web::http::header::HeaderValue> = req.headers().get(JWT_TOKEN_HEADER);
        match result {
            Some(header) => {
                //println!("Found header [{}] ", header);
                header.to_str().unwrap()[JWT_TOKEN_HEADER_SUFFIX.len()..].to_string()
            }
            None => {
                "".to_string()
            }
        }
    }

}

#[cfg(test)]
mod test {

    extern crate actix_web;
    extern crate serde_json;

    use self::actix_web::{http, HttpMessage};
    use super::super::server::{Server, Router};
    use test_root;
    use std::str;

    #[test]
    fn should_correctly_parse_the_auth_token() {
        let config = super::super::config::ServerConfig { port: 0 };
        let server = Server::new(&config);

        server.register(Box::new(TestRouter{}));

        let mut test_server = test_root::new_test_server(&server);

        let token = "random_token_value";

        let request_one = test_server
            .client(http::Method::GET, "/header/echo")
            .header(super::JWT_TOKEN_HEADER, format!("{}{}", super::JWT_TOKEN_HEADER_SUFFIX,token))
            .finish().unwrap();
        let response: actix_web::client::ClientResponse = test_server.execute(request_one.send()).unwrap();

        println!("Response [{:?}]", response);
        assert!(response.status().is_success());

        let bytes = test_server.execute(response.body()).unwrap();
        let body_str = str::from_utf8(&bytes).unwrap();
        let body: MyObj = serde_json::from_str(body_str).unwrap();

        println!("body: {:?}", body);

        assert_eq!(token, body.value);
    }

    struct TestRouter{}

    impl Router for TestRouter{
        fn configure(&self) -> actix_web::App {
            actix_web::App::new().prefix("/header")
                .resource("/echo",move |r|
                    r.f(move |req| {
                        let service = super::new();
                        let token = service.token_from(req);
                        println!("Found token [{}]", token);
                        format!("{}", token);
                        actix_web::HttpResponse::Ok().json(MyObj{value: token})
                    }))
        }
    }

    #[derive(Serialize, Deserialize, Debug)]
    struct MyObj {
        value: String,
    }
}