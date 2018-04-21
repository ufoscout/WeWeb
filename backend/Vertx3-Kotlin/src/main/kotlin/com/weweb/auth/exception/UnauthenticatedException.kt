package com.weweb.auth.exception

/**
 * Unauthorized Exception thrown when an invalid user calls a protected end point
 *
 * @author Francesco Cina'
 */
class UnauthenticatedException(message: String) : RuntimeException(message) {
    companion object {
        private val serialVersionUID = 1L
    }
}