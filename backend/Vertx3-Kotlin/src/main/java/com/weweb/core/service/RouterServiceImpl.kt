package com.weweb.core.service

import com.weweb.core.config.CoreConfig
import com.weweb.core.exception.ErrorDetails
import com.weweb.core.exception.WebException
import com.weweb.core.exception.WebExceptionService
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.awaitResult
import java.util.*

class RouterServiceImpl(val vertx: Vertx, val coreConfig: CoreConfig, val webExceptionService: WebExceptionService) : RouterService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val router = Router.router(vertx)

    init {
        //router.route().consumes("application/json")
        //router.route().produces("application/json")
        //router.route().handler(BodyHandler.create())
        router.route().failureHandler { handleFailure(it) }
    }

    override fun router(): Router {
        return router
    }

    override suspend fun start() {
        awaitResult<HttpServer> { wait ->
            val port = coreConfig.server.port;
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { router.accept(it) }).listen(port, wait)
            logger.info("Router created and listening on port ${port}")
        }
    }

    private fun handleFailure(context: RoutingContext) {
        logger.info("Handling failure")
        val exception = context.failure()
        val response = context.response()

        if (exception is WebException) {
            reply(response, exception)
        } else {
            val webEx = webExceptionService.get(exception)
            if (webEx != null) {
                reply(response, webEx)
            } else {
                var statusCode = context.statusCode()
                if (statusCode < 0) {
                    statusCode = 500
                }
                val uuid = UUID.randomUUID().toString()
                val message = "Error code: " + uuid
                logger.error(uuid + " : " + exception.message, exception)
                response.setStatusCode(statusCode).endWithJson(ErrorDetails(statusCode, message))
            }
        }

    }

    private fun reply(response: HttpServerResponse, exception: WebException) {
        val statusCode = exception.statusCode()
        response.setStatusCode(statusCode).endWithJson(ErrorDetails(statusCode, exception.message!!))
    }

}
