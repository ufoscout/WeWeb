package io.vertx.starter.config

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class ConfigModule(val config: JsonObject) : VertxkModule() {

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

    override fun module() = Kodein.Module {
        bind<AppConfig>() with singleton { AppConfig(serverPort = config.getJsonObject("server").getInteger("port", 8080)) }
    }

}