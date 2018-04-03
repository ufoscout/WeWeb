package io.vertx.starter

import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.starter.config.AppConfig

class MainVerticle (val appConfig: AppConfig, val router: Router) : CoroutineVerticle() {

    companion object {
        val HELLO_MSG = "Hello Vert.x from kotlin verticle!"
    }

    override suspend fun start() {

        println("Start main verticle")

        val port = appConfig.serverPort();
        router.get("/").handler { it.response().end(HELLO_MSG) }

        // Create the http server and pass it the router

        awaitResult<HttpServer> { wait ->
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { router.accept(it) }).listen(port, wait)
        }

        println("Server listening on port " + port)

    }

}