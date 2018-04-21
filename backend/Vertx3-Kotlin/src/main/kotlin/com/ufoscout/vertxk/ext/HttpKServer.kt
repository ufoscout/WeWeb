package com.ufoscout.vertxk.ext

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

interface HttpKServer {

    fun HttpServerResponse.endWithJson(obj: Any) {
        this.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(obj))
    }

}