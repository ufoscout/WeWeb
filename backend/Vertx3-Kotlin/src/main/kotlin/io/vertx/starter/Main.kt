package io.vertx.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.jxinject.jxInjectorModule
import com.ufoscout.vertxk.VertxkKodein
import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.starter.config.ConfigModule
import kotlinx.coroutines.experimental.runBlocking
import java.io.IOException


object Main {

    // initiate logging system
    private val log = LoggerFactory.getLogger(Main::class.java)

    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {

        println("Starting kotlin main")

        runBlocking {
            launch(Kodein.Module{})
        }

    }

    suspend fun launch(overrides: Kodein.Module): Kodein {

        // setup vertx
        val vertx = Vertx.vertx()

        var retriever = ConfigRetriever.create(vertx)
        var config = awaitResult<JsonObject> { wait ->
            retriever.getConfig(wait);
        }

        val kodein = Kodein {
            import(jxInjectorModule)
            import(VertxkKodein.module(vertx))
            import(ConfigModule.module(config))
            import(AppModule.module(vertx))
            import(overrides, allowOverride = true)
        }

        VertxkKodein.registerFactory(vertx, kodein)

        awaitResult<String> {
            VertxkKodein.deployVerticle<MainVerticle>(vertx, it)
        }

        return kodein
    }
}