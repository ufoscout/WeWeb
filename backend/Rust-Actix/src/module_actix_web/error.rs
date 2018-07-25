extern crate actix_web;
extern crate failure;

use ::auth;
use ::jwt;
use std::collections::HashMap;

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ErrorDetails {
    pub code: u16,
    pub message: String,
    pub details: HashMap<String, Vec<String>>,
}

impl actix_web::error::ResponseError for auth::AuthError {
    fn error_response(&self) -> actix_web::HttpResponse {
        match *self {
            auth::AuthError::UnAuthenticatedError {} => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::UNAUTHORIZED)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::UNAUTHORIZED.as_u16(),
                    message: "NotAuthenticated".to_string(),
                    details: HashMap::new()
                }),
            auth::AuthError::NoRequiredPermission { permission: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::FORBIDDEN)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::FORBIDDEN.as_u16(),
                    message: "AccessDenied".to_string(),
                    details: HashMap::new()
                }),
            auth::AuthError::NoRequiredRole { role: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::FORBIDDEN)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::FORBIDDEN.as_u16(),
                    message: "AccessDenied".to_string(),
                    details: HashMap::new()
                }),
        }
    }
}

impl actix_web::error::ResponseError for jwt::JwtError {
    fn error_response(&self) -> actix_web::HttpResponse {
        match *self {
            jwt::JwtError::ExpiredTokenError { message: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::UNAUTHORIZED)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::UNAUTHORIZED.as_u16(),
                    message: "TokenExpired".to_string(),
                    details: HashMap::new()
                }),
            jwt::JwtError::InvalidTokenError { message: _ } => actix_web::HttpResponse::build(
                actix_web::http::StatusCode::UNAUTHORIZED)
                .json(ErrorDetails{
                    code: actix_web::http::StatusCode::UNAUTHORIZED.as_u16(),
                    message: "InvalidToken".to_string(),
                    details: HashMap::new()
                }),
            jwt::JwtError::GenerateTokenError { message: _ } => actix_web::HttpResponse::new(
                actix_web::http::StatusCode::BAD_REQUEST)
        }
    }
}
