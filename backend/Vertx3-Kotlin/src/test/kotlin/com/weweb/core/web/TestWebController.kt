package com.weweb.core.web

import com.weweb.core.exception.WebException
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class TestWebController(val router: Router) : CoroutineVerticle() {

    override suspend fun start() {

        router.get("/core/test/fatal/:message").handler {
            var message = it.request().getParam("message")
            throw Exception(message)
        }

        router.get("/core/test/webException/:code/:message").handler {
            var code = it.request().getParam("code")
            var message = it.request().getParam("message")
            throw WebException(message, code.toInt())
        }

    }

}