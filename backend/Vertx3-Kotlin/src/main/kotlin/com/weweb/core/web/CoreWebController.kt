package com.weweb.core.web

import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import com.weweb.core.CoreConfig

class CoreWebController (val appConfig: CoreConfig, val router: Router) : CoroutineVerticle() {

    override suspend fun start() {

        val port = appConfig.serverPort();
        // Create the http server and pass it the router
        awaitResult<HttpServer> { wait ->
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { router.accept(it) }).listen(port, wait)
        }
        println(Thread.currentThread().name + " - Server listening on port " + port)
    }

}