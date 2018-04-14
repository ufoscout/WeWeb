package com.weweb.core.exception

class WebException : RuntimeException {

    private val statusCode: Int


    constructor(message: String = "", code: Int = 500) : super(message) {
        statusCode = code
    }

    constructor(message: String = "", cause: Throwable, code: Int = 500) : super(message, cause) {
        statusCode = code
    }

    public fun statusCode(): Int = statusCode
    
}
