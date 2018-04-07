package io.vertx.starter.config

import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import io.vertx.core.json.JsonObject
import io.vertx.starter.vertxk.VertxkModule

class ConfigModule(val config: JsonObject) : VertxkModule() {

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

    override fun module() = Kodein.Module {
        bind<AppConfig>() with singleton { AppConfig(serverPort = config.getJsonObject("server").getInteger("port", 8080)) }
    }

}