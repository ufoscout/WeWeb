package io.vertx.starter.config

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import io.vertx.core.json.JsonObject

object ConfigModule {

        fun module(config: JsonObject) = Kodein.Module {

            bind<AppConfig>() with singleton { AppConfig(serverPort = config.getJsonObject("server").getInteger("port", 8080)) }

        }

}