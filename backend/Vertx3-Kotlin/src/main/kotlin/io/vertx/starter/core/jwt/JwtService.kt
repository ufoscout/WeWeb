package io.vertx.starter.core.jwt

/**
 * Interface to parse and generate JWTs
 *
 * @author Francesco Cina'
 */
interface JwtService {

    /**
     * Generates a JWT from the payload
     * @param payload the JWT payload
     * @return
     */
    fun <T> generate(payload: T): String

    /**
     * Generates a JWT from the payload
     * @param subject the JWT subject
     * @param payload the JWT payload
     * @return
     */
    fun <T> generate(subject: String, payload: T): String

    /**
     * Parses a JWT and return the contained bean.
     * It throws [TokenExpiredException] if the token has expired.
     *
     * @param jwt
     * @param payloadClass
     * @return
     */
    @Throws(TokenExpiredException::class)
    fun <T> parse(jwt: String, payloadClass: Class<T>): T

}
