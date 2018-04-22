package com.ufoscout.vertxk.ext

import io.vertx.core.http.HttpClient

interface HttpClientExt {

    fun HttpClient.k(): HttpClientK {
        return HttpClientK(this)
    }

}