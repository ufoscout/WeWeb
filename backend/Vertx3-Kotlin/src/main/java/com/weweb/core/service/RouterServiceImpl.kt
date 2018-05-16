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
import io.vertx.kotlin.coroutines.awaitResult
import java.util.*

class RouterServiceImpl(val vertx: Vertx, val coreConfig: CoreConfig, val webExceptionService: WebExceptionService) : RouterService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private var mainRouter: Router? = null;
    }

    init {
        if (mainRouter==null) {
            synchronized(RouterServiceImpl) {
                if (mainRouter==null) {
                    println("--------------------------")
                    println("creating singleton router!")
                    println("--------------------------")
                    mainRouter = Router.router(vertx)
                }
            }
        }

        mainRouter!!.route().failureHandler { handleFailure(it) }
    }

    override fun router(): Router {
        val router = Router.router(vertx)
        return mainRouter!!.mountSubRouter("/", router)
    }

    override suspend fun start() {
        awaitResult<HttpServer> { wait ->
            val port = coreConfig.server.port;
            vertx.createHttpServer().requestHandler(Handler<HttpServerRequest> { mainRouter!!.accept(it) }).listen(port, wait)
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
