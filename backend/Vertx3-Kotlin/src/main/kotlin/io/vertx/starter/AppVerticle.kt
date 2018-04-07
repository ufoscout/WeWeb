package io.vertx.starter

import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.starter.config.AppConfig

class AppVerticle (val appConfig: AppConfig, val router: Router) : CoroutineVerticle() {

    override suspend fun start() {

        println("Start main verticle")

        val port = appConfig.serverPort();
        // Create the http server and pass it the router
        awaitResult<HttpServer> { wait ->
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { router.accept(it) }).listen(port, wait)
        }
        println(Thread.currentThread().name + " - Server listening on port " + port)
    }

}