package com.ufoscout.vertxk.ext

class HttpClientKResponse<T>(
        val statusCode: Int,
        val body: T
) {
}