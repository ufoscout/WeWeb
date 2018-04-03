package io.vertx.starter.config

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import io.vertx.core.json.JsonObject

object ConfigModule {

        fun module(config: JsonObject) = Kodein.Module {

            bind<AppConfig>() with singleton { AppConfig(serverPort = config.getJsonObject("server").getInteger("port", 8080)) }

        }


}