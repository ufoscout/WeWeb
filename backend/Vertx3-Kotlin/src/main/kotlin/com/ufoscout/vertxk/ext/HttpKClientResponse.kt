package com.ufoscout.vertxk.ext

class HttpKClientResponse<T>(
        val statusCode: Int,
        val body: T
) {
}