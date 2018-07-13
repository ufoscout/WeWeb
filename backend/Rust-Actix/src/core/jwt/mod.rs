
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

#[derive(Debug, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Token<T> {

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

#[derive(Fail, Debug)]
#[fail(display = "JwtError: [{}]", message)]
pub struct JwtError {
    message: String,
    kind: JwtErrorKind
}

#[derive(Debug)]
pub enum JwtErrorKind {
    InvalidTokenError,
    ExpiredTokenError,
    GenerateTokenError,
}


impl JwtService {

    pub fn generate_from_payload<T: serde::ser::Serialize>(&self, payload: &T) -> Result<String, JwtError> {
        let issued_at = time::get_time().sec;
        let token = Token{
            payload,
            sub: "".to_string(),
            exp: issued_at + self.token_validity_seconds,
            iat: issued_at,
        };
        self.generate_from_token(&token)
    }

    pub fn generate_from_token<T: serde::ser::Serialize>(&self, token: &Token<T>) -> Result<String, JwtError> {
        let result = jsonwebtoken::encode(&self.header_default, &token, &self.secret.as_ref());
        match result {
            Ok(t) => Ok(t),
            Err(e) => {
                //let err = e.to_string();
                Err(JwtError{
                    message: e.to_string(),
                    kind: JwtErrorKind::GenerateTokenError
                })
            }
        }
    }

    pub fn parse_payload<T: serde::de::DeserializeOwned>(&self, jwt_string: &str) -> Result<T, JwtError> {
        let result = self.parse_token(jwt_string);
        match result {
            Ok(t) => Ok(t.payload),
            Err(e) => {
                Err(e)
            }
        }
    }

    pub fn parse_token<T: serde::de::DeserializeOwned>(&self, jwt_string: &str) -> Result<Token<T>, JwtError> {
        let result: Result<jsonwebtoken::TokenData<Token<T>>, jsonwebtoken::errors::Error> =
            jsonwebtoken::decode(jwt_string, &self.secret.as_ref(), &self.validation_default);
        match result {
            Ok(t) => Ok(t.claims),
            Err(e) => match *e.kind() {
                jsonwebtoken::errors::ErrorKind::ExpiredSignature =>
                    Err(JwtError{
                        message: e.to_string(),
                        kind: JwtErrorKind::ExpiredTokenError
                    }),
                _ => Err(JwtError{
                    message: e.to_string(),
                    kind: JwtErrorKind::InvalidTokenError
                })
            }
        }
    }

}

#[cfg(test)]
mod test {

    extern crate time;
    extern crate failure;

    use test_root::IT_CONTEXT;

    #[test]
    fn should_create_jwt_string_from_token() {

        let jwt = &IT_CONTEXT.core.jwt;

        let payload = MyTestClaym {
            id: time::get_time().sec,
            name: "Red".to_string()
        };

        let token = super::Token {
            payload,
            sub: "".to_string(),
            exp: time::get_time().sec + 3600,
            iat: time::get_time().sec,
        };

        let jwt_string = jwt.generate_from_token(&token).unwrap();
        println!("Jwt string: [{}]", jwt_string);

    }

    #[test]
    fn should_create_jwt_string_from_payload() {

        let jwt = &IT_CONTEXT.core.jwt;

        let payload = MyTestClaym {
            id: time::get_time().sec,
            name: "Red".to_string()
        };

        let jwt_string = jwt.generate_from_payload(&payload).unwrap();
        println!("Jwt string: [{}]", jwt_string);

    }

    #[test]
    fn should_parse_the_token() {

        let jwt = &IT_CONTEXT.core.jwt;

        let payload = MyTestClaym {
            id: time::get_time().sec,
            name: "Red".to_string()
        };

        let jwt_string = jwt.generate_from_payload(&payload).unwrap();
        let parsed : MyTestClaym = jwt.parse_payload(&jwt_string).unwrap();

        assert_eq!(payload.id, parsed.id);
        assert_eq!(payload.name, parsed.name);

    }

    #[test]
    fn should_parse_the_expiration_date() {

        let jwt = &IT_CONTEXT.core.jwt;

        let payload = MyTestClaym {
            id: time::get_time().sec,
            name: "Red".to_string()
        };

        let time_before = time::get_time().sec;
        let jwt_string = jwt.generate_from_payload(&payload).unwrap();
        let time_after = time::get_time().sec;

        let token : super::Token<MyTestClaym> = jwt.parse_token(&jwt_string).unwrap();

        assert_eq!(payload.id, token.payload.id);
        assert_eq!(&payload.name, &token.payload.name);

        let issued_at = token.iat;
        let expiration = token.exp;
        let timeout = (IT_CONTEXT.core.config.jwt.token_validity_minutes as i64) * 60;

        assert!(issued_at >= time_before);
        assert!(issued_at <= time_after);
        assert_eq!(issued_at + timeout, expiration);
    }

    #[test]
    fn should_fail_parsing_tampered_token() {

        let jwt = &IT_CONTEXT.core.jwt;

        let payload = MyTestClaym {
            id: time::get_time().sec,
            name: "Red".to_string()
        };

        let mut jwt_string = jwt.generate_from_payload(&payload).unwrap();
        jwt_string.push_str("1");

        let result : Result<super::Token<MyTestClaym>, super::JwtError> = jwt.parse_token(&jwt_string);
        let mut is_invalid = false;
        match result {
            Ok(r) => println!("Ok: {:?}", r),
            Err(e) => match e.kind {
                super::JwtErrorKind::InvalidTokenError => {
                    println!("Invalid: {:?}", e);
                    is_invalid = true;
                },
                _ => println!("Other kind of error: {:?}", e)
            }
        };
        assert!(is_invalid)
    }

    #[test]
    fn should_fail_parsing_expired_token() {

        let jwt = &IT_CONTEXT.core.jwt;

        let token = super::Token {
            payload: MyTestClaym {
                id: time::get_time().sec,
                name: "Red".to_string()
                },
            sub: "".to_string(),
            exp: time::get_time().sec - 10,
            iat: time::get_time().sec - 100,
        };

        let jwt_string = jwt.generate_from_token(&token).unwrap();

        let result : Result<MyTestClaym, super::JwtError> = jwt.parse_payload(&jwt_string);
        let mut is_expired = false;
        match result {
          Ok(r) => println!("Ok: {:?}", r),
          Err(e) => match e.kind {
              super::JwtErrorKind::ExpiredTokenError => {
                  println!("Expired: {:?}", e);
                  is_expired = true;
              },
              _ => println!("Other kind of error: {:?}", e)
          }
        };
        assert!(is_expired)

    }

    #[derive(Debug, Serialize, Deserialize)]
    struct MyTestClaym {
        id: i64,
        name: String
    }
}