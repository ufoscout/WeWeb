package com.weweb.core.web

import com.weweb.core.CoreConfig
import com.weweb.core.exception.WebException
import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import java.util.*


class CoreWebController (val appConfig: CoreConfig, val router: Router) : CoroutineVerticle() {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun start() {

        router.route().failureHandler {
            logger.info("Handling failure")
            val exception = it.failure()
            val response = it.response()
            var statusCode = it.statusCode()
            var message = ""

            if (exception is WebException) {
                statusCode = exception.statusCode()
                message = exception.message!!
            } else if (statusCode==500 || statusCode==-1) {
                statusCode = 500
                val uuid = UUID.randomUUID().toString()
                message = "Error code: " + uuid
                logger.error(uuid + " : " + exception.message, exception)
            }

            response.setStatusCode(statusCode).end(message)

        }

        val port = appConfig.serverPort();
        // Create the http server and pass it the router
        awaitResult<HttpServer> { wait ->
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { router.accept(it) }).listen(port, wait)
        }
        println(Thread.currentThread().name + " - Server listening on port " + port)
    }

}