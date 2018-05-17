package com.weweb.core.jwt

import java.util.*
import kotlin.reflect.KClass

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
     * Generates a JWT from the payload
     * @param subject the JWT subject
     * @param payload the JWT payload
     * @param createdDate the creation Date
     * @param expirationDate the expiration Date
     * @return
     */
    fun generate(subject: String, payload: Any, createdDate: Date, expirationDate: Date): String

    /**
     * Parses a JWT and return the contained bean.
     * It throws [TokenExpiredException] if the token has expired.
     *
     * @param jwt
     * @param payloadClass
     * @return
     */
    @Throws(TokenExpiredException::class)
    fun <T: Any> parse(jwt: String, payloadClass: KClass<T>): T

}

/**
 * Parses a JWT and return the contained bean.
 * It throws [TokenExpiredException] if the token has expired.
 *
 * @param jwt
 * @return
 */
@Throws(TokenExpiredException::class)
inline fun <reified T: Any> JwtService.parse(jwt: String) = parse(jwt, T::class)