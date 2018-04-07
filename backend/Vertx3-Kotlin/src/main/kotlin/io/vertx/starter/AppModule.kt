package io.vertx.starter

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Created by ufo on 18/07/17.
 */

class AppModule: VertxkModule() {

    override fun module() = Kodein.Module {

        bind<Router>() with singleton {
            println("create router")
            val mainRouter = Router.router(instance())
            mainRouter.route().consumes("application/json")
            mainRouter.route().produces("application/json")
            mainRouter.route().handler(BodyHandler.create())
            //mainRouter.route().failureHandler(GlobalHandlers::error);
            mainRouter
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        deployVerticle<AppVerticle>(vertx)
    }

}