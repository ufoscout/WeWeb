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
    fun generate(payload: Any): String

    /**
     * Generates a JWT from the payload
     * @param subject the JWT subject
     * @param payload the JWT payload
     * @return
     */
    fun generate(subject: String, payload: Any): String

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
