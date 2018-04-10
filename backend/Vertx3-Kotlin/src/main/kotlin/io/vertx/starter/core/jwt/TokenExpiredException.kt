package io.vertx.starter.core.jwt

/**
 * Exception thrown when parsing an expired jwt token.
 *
 * @author Francesco Cina'
 */
class TokenExpiredException : RuntimeException() {
    companion object {

        private val serialVersionUID = 1L
    }

}
