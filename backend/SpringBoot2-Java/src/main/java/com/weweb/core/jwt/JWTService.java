package com.weweb.core.jwt;

/**
 * Interface to parse and generate JWTs
 *
 * @author Francesco Cina'
 *
 */
public interface JWTService {

    /**
     * Generates a JWT from the payload
     * @param payload the JWT payload
     * @return
     */
    <T> String generate(T payload);

    /**
	 * Generates a JWT from the payload
     * @param subject the JWT subject
	 * @param payload the JWT payload
	 * @return
	 */
    <T> String generate(String subject, T payload);

    /**
     * Parses a JWT and return the contained bean.
     * It throws {@link TokenExpiredException} if the token has expired.
     *
     * @param jwt
     * @param payloadClass
     * @return
     */
    <T> T parse(String jwt, Class<T> payloadClass);

}
