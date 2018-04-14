package com.weweb.core.web

import com.weweb.core.CoreConfig
import com.weweb.core.exception.WebException
import com.weweb.core.exception.WebExceptionService
import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import java.util.*


class CoreWebController (val appConfig: CoreConfig, val router: Router, val webExceptionService: WebExceptionService) : CoroutineVerticle() {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun start() {

        router.route().failureHandler { handleFailure(it) }

        val port = appConfig.serverPort();
        // Create the http server and pass it the router
        awaitResult<HttpServer> { wait ->
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { router.accept(it) }).listen(port, wait)
        }
        println(Thread.currentThread().name + " - Server listening on port " + port)
    }

    private fun handleFailure(context: RoutingContext) {
        logger.info("Handling failure")
        val exception = context.failure()
        val response = context.response()

        if (exception is WebException) {
            reply(response, exception)
        } else {
            val webEx = webExceptionService.get(exception)
            if ( webEx!=null ) {
                reply(response, webEx)
            }
            else {
                var statusCode = context.statusCode()
                if (statusCode<0) { statusCode = 500 }
                val uuid = UUID.randomUUID().toString()
                val message = "Error code: " + uuid
                logger.error(uuid + " : " + exception.message, exception)
                response.setStatusCode(statusCode).end(message)
            }
        }

    }

    private fun reply(response: HttpServerResponse, exception: WebException) {
        val statusCode = exception.statusCode()
        val message = exception.message!!
        response.setStatusCode(statusCode).end(message)
    }

}
