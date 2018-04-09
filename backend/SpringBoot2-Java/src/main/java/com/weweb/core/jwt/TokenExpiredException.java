package com.weweb.core.jwt;

/**
 * Exception thrown when parsing a bean that implements {@link Expirable} that has expired
 *
 * @author Francesco Cina'
 *
 */
public class TokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

}
