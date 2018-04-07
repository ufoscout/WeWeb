package io.vertx.starter

import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class HelloController (val router: Router) : CoroutineVerticle() {

    companion object {
        val HELLO_MSG = "Hello Vert.x from kotlin verticle!"
    }

    override suspend fun start() {

        router.get("/").handler { it.response().end(HELLO_MSG) }

    }

}