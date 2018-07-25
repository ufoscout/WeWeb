extern crate actix_web;
extern crate failure;
extern crate coreutils_auth as auth;
extern crate coreutils_jwt as jwt;

use std::collections::HashMap;

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ErrorDetails {
    pub code: u16,
    pub message: String,
    pub details: HashMap<String, Vec<String>>,
}

#[derive(Fail, Debug)]
pub enum WebAuthError {
    #[fail(display = "UnAuthenticatedError")]
    UnAuthenticatedError {},
    #[fail(display = "NoRequiredRole [{}]", role)]
    NoRequiredRole { role: String },
    #[fail(display = "NoRequiredPermission [{}]", permission)]
    NoRequiredPermission { permission: String },
}

impl actix_web::error::ResponseError for WebAuthError {
    fn error_response(&self) -> actix_web::HttpResponse {
        match *self {
            WebAuthError::UnAuthenticatedError {} => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::UNAUTHORIZED)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::UNAUTHORIZED.as_u16(),
                    message: "NotAuthenticated".to_string(),
                    details: HashMap::new()
                }),
            WebAuthError::NoRequiredPermission { permission: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::FORBIDDEN)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::FORBIDDEN.as_u16(),
                    message: "AccessDenied".to_string(),
                    details: HashMap::new()
                }),
            WebAuthError::NoRequiredRole { role: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::FORBIDDEN)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::FORBIDDEN.as_u16(),
                    message: "AccessDenied".to_string(),
                    details: HashMap::new()
                }),
        }
    }
}

#[derive(Fail, Debug)]
pub enum WebJwtError {
    #[fail(display = "InvalidTokenError: [{}]", message)]
    InvalidTokenError { message: String },
    #[fail(display = "ExpiredTokenError: [{}]", message)]
    ExpiredTokenError { message: String },
    #[fail(display = "GenerateTokenError: [{}]", message)]
    GenerateTokenError { message: String },
}

impl actix_web::error::ResponseError for WebJwtError {
    fn error_response(&self) -> actix_web::HttpResponse {
        match *self {
            WebJwtError::ExpiredTokenError { message: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::UNAUTHORIZED)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::UNAUTHORIZED.as_u16(),
                    message: "TokenExpired".to_string(),
                    details: HashMap::new()
                }),
            WebJwtError::InvalidTokenError { message: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::UNAUTHORIZED)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::UNAUTHORIZED.as_u16(),
                    message: "InvalidToken".to_string(),
                    details: HashMap::new()
                }),
            WebJwtError::GenerateTokenError { message: _ } => actix_web::HttpResponse::new(
                actix_web::http::StatusCode::BAD_REQUEST)
        }
    }
}
