
extern crate serde;
extern crate time;
extern crate failure;
extern crate jsonwebtoken;

use core;

pub fn new(jwt_config: &core::config::JwtConfig ) -> JwtService {
    let alg= alg_from_str(&jwt_config.signature_algorithm);

    JwtService{
        secret: jwt_config.secret.clone(),
        token_validity_seconds: (jwt_config.token_validity_minutes as i64) * 60,
        header_default: jsonwebtoken::Header {
            alg,
            ..jsonwebtoken::Header::default()
        },
        validation_default: jsonwebtoken::Validation::new(alg)
    }
}

fn alg_from_str(s: &str) -> jsonwebtoken::Algorithm {
    match s {
        "HS256" => jsonwebtoken::Algorithm::HS256,
        "HS384" => jsonwebtoken::Algorithm::HS384,
        "HS512" => jsonwebtoken::Algorithm::HS512,
        "RS256" => jsonwebtoken::Algorithm::RS256,
        "RS384" => jsonwebtoken::Algorithm::RS384,
        "RS512" => jsonwebtoken::Algorithm::RS512,
        _ => panic!("Unknown JWT signature algorithm: [{}]", s),
    }
}

pub struct JwtService {
    secret: String,
    token_validity_seconds: i64,
    header_default: jsonwebtoken::Header,
    validation_default: jsonwebtoken::Validation
}

#[derive(Serialize, Deserialize)]
pub struct Token<T: serde::ser::Serialize> {

    payload: T,

    // The subject of the token
    sub: String,
    // The expiration date of the token
    exp: i64,
    // The issued at field
    iat: i64,
    // The token id
    //jti: String,
}

impl JwtService {

    pub fn generate_from_clayms<T: serde::ser::Serialize>(&self, clayms: &T) -> Result<String, failure::Error> {
        let token = Token{
            payload: clayms,
            sub: "".to_string(),
            exp: time::get_time().sec + self.token_validity_seconds,
            iat: time::get_time().sec,
        };
        self.generate_from_token(&token)
    }

    pub fn generate_from_token<T: serde::ser::Serialize>(&self, clayms: &Token<T>) -> Result<String, failure::Error> {
        let result = jsonwebtoken::encode(&self.header_default, &clayms, &self.secret.as_ref());
        match result {
            Ok(t) => Ok(t),
            Err(e) => {
                //let err = e.to_string();
                Err(format_err!("{}", e))
            }
        }
    }

}

#[cfg(test)]
mod test {

    extern crate time;

    use test_root::IT_CONTEXT;

    #[test]
    fn should_create_jwt_string_from_token() {

        let jwt = &IT_CONTEXT.core.jwt;

        let claym = MyTestClaym {
            id: 123,
            name: "Red".to_string()
        };

        let token = super::Token {
            payload: claym,
            sub: "".to_string(),
            exp: time::get_time().sec + 3600,
            iat: time::get_time().sec,
        };

        let jwt_string = jwt.generate_from_token(&token).unwrap();
        println!("Jwt string: [{}]", jwt_string);

    }

    #[test]
    fn should_create_jwt_string_from_clayms() {

        let jwt = &IT_CONTEXT.core.jwt;

        let claym = MyTestClaym {
            id: 123,
            name: "Red".to_string()
        };

        let jwt_string = jwt.generate_from_clayms(&claym).unwrap();
        println!("Jwt string: [{}]", jwt_string);

    }

    #[derive(Serialize, Deserialize)]
    struct MyTestClaym {
        id: i64,
        name: String
    }
}