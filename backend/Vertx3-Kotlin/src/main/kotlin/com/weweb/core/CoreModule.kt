package com.weweb.core

import com.ufoscout.vertxk.VertxkModule
import com.weweb.core.exception.WebExceptionService
import com.weweb.core.exception.WebExceptionServiceImpl
import com.weweb.core.service.RouterService
import com.weweb.core.service.RouterServiceImpl
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CoreModule(val config: JsonObject) : VertxkModule() {

    override fun module() = Kodein.Module {
            bind<CoreConfig>() with singleton { CoreConfig(serverPort = config.getJsonObject("server").getInteger("port", 8080)) }
            bind<WebExceptionService>() with singleton { WebExceptionServiceImpl() }
            bind<RouterService>() with singleton {RouterServiceImpl(instance(), instance())}
            bind<DeploymentOptions>() with provider {
                val options = DeploymentOptions()
                options.instances = Runtime.getRuntime().availableProcessors()
                options
            }
        }

    override suspend fun onInit(vertx: Vertx, kodein: DKodein) {
        //val options = DeploymentOptions()
        //options.setInstances(2)
        //deployVerticle<CoreWebController>(vertx, options)
    }

}