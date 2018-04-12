package com.weweb.core

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import com.weweb.core.web.CoreWebController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CoreModule(val config: JsonObject) : VertxkModule() {

    override fun module() = Kodein.Module {
        bind<CoreConfig>() with singleton { CoreConfig(serverPort = config.getJsonObject("server").getInteger("port", 8080)) }
        bind<Router>() with singleton {
            val mainRouter = Router.router(instance())
            mainRouter.route().consumes("application/json")
            mainRouter.route().produces("application/json")
            mainRouter.route().handler(BodyHandler.create())
            //mainRouter.route().failureHandler(GlobalHandlers::error);
            mainRouter
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        deployVerticle<CoreWebController>(vertx)
    }

}