extern crate actix_web;
extern crate failure;

use std::sync::Arc;
use ::auth;
use ::jwt;

static JWT_TOKEN_HEADER: &str = "Authorization";
static JWT_TOKEN_HEADER_SUFFIX: &str = "Bearer ";

pub fn new(auth_service: Arc<auth::AuthService>, jwt_service: Arc<jwt::JwtService>) -> AuthContextService {
    AuthContextService {
        auth_service,
        jwt_service
    }
}

pub struct AuthContextService {
    jwt_service: Arc<jwt::JwtService>,
    auth_service: Arc<auth::AuthService>
}

impl AuthContextService {

    pub fn token_from(&self, req: &actix_web::HttpRequest) -> String {
        let result: Option<&actix_web::http::header::HeaderValue> = req.headers().get(JWT_TOKEN_HEADER);
        match result {
            Some(header) => {
                let jwt = header.to_str().unwrap();
                if jwt.len() > JWT_TOKEN_HEADER_SUFFIX.len() {
                    jwt[JWT_TOKEN_HEADER_SUFFIX.len()..].to_string()
                } else {
                    "".to_string()
                }
            }
            None => {
                "".to_string()
            }
        }
    }

    pub fn from(&self, req: &actix_web::HttpRequest) -> auth::AuthContext {
        let token = self.token_from(req);
        self.from_token(&token)
    }

    pub fn from_token(&self, token_string: &str) -> auth::AuthContext {
        let auth = match self.jwt_service.parse_payload(token_string) {
            Ok(a) => a,
            Err(_) => auth::model::Auth {
                id: -1,
                username: "".to_string(),
                roles: vec![]
            }
        };
        self.from_auth(auth)
    }

    pub fn from_auth(&self, user_auth: auth::model::Auth) -> auth::AuthContext {
        self.auth_service.auth(user_auth)
    }

}

#[cfg(test)]
mod test {

    extern crate actix_web;
    extern crate serde_json;

    use ::auth as auth_s;
    use ::jwt;
    use self::actix_web::{http, HttpMessage};
    use super::super::server::{Server, Router};
    use test_root;
    use std::str;
    use std::sync::Arc;

    #[test]
    fn should_correctly_parse_the_auth_token() {
        let config = super::super::config::ServerConfig { port: 0, shutdown_timeout: 60 };
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

    #[test]
    fn should_get_the_auth_from_headers() {
        let config = super::super::config::ServerConfig { port: 0, shutdown_timeout: 60 };
        let server = Server::new(&config);
        server.register(Box::new(TestRouter{}));
        let mut test_server = test_root::new_test_server(&server);

        let service = new_service();
        let auth = auth_s::model::Auth {
                id: 123456,
                username: "username_test".to_string(),
                roles: vec!["roleOne".to_string()]
            };
        let token = service.jwt_service.generate_from_payload(&auth).unwrap();

        let request_one = test_server
            .client(http::Method::GET, "/header/auth")
            .header(super::JWT_TOKEN_HEADER, format!("{}{}", super::JWT_TOKEN_HEADER_SUFFIX,token))
            .finish().unwrap();
        let response: actix_web::client::ClientResponse = test_server.execute(request_one.send()).unwrap();

        println!("Response [{:?}]", response);
        assert!(response.status().is_success());

        let bytes = test_server.execute(response.body()).unwrap();
        let body_str = str::from_utf8(&bytes).unwrap();
        let body: auth_s::model::Auth = serde_json::from_str(body_str).unwrap();

        println!("body: {:?}", body);

        assert_eq!(auth.id, body.id);
        assert_eq!(auth.username, body.username);
        assert_eq!(auth.roles.len(), body.roles.len());
    }

    fn new_service() -> super::AuthContextService {
        let roles_provider = auth_s::InMemoryRolesProvider::new(vec![]);
        let auth_service = auth_s::new(Box::new(roles_provider));
        let jwt_service = jwt::new(&jwt::config::JwtConfig{
            secret: "secret".to_string(),
            signature_algorithm: "HS512".to_string(),
            token_validity_minutes: 60
        });
        super::new(Arc::new(auth_service), Arc::new(jwt_service))
    }


    struct TestRouter{}

    impl Router for TestRouter{
        fn configure(&self) -> actix_web::App {
            actix_web::App::new().prefix("/header")
                .resource("/echo",move |r|
                    r.f(move |req| {
                        let service = new_service();
                        let token = service.token_from(req);
                        println!("Found token [{}]", token);
                        actix_web::HttpResponse::Ok().json(MyObj{value: token})
                    }))
                .resource("/auth",move |r|
                    r.f(move |req| {
                        let service = new_service();
                        let auth = service.from(req).auth;
                        println!("Found auth [{:?}]", auth);
                        actix_web::HttpResponse::Ok().json(auth)
                    }))
        }
    }

    #[derive(Serialize, Deserialize, Debug)]
    struct MyObj {
        value: String,
    }
}