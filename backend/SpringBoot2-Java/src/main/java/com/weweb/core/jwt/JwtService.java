package com.weweb.core.jwt;

import java.util.Date;

/**
 * Interface to parse and generate JWTs
 *
 * @author Francesco Cina'
 *
 */
public interface JwtService {

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
     * Generates a JWT from the payload
     * @param subject the JWT subject
     * @param payload the JWT payload
     * @param createdDate the creation Date
     * @param expirationDate the expiration Date
     * @return
     */
    <T> String generate(final String subject, final T payload, Date createdDate, Date expirationDate);

    /**
     * Parses a JWT and return the contained bean.
     * It throws {@link TokenExpiredException} if the token has expired.
     *
     * @param jwt
     * @param payloadClass
     * @return
     */
    <T> T parse(String jwt, Class<T> payloadClass) throws TokenExpiredException;

}
