package io.vertx.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.jxinject.jxInjectorModule
import com.ufoscout.vertxk.VertxkKodein
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
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

        val kodein = Kodein {
            import(jxInjectorModule)
            import(VertxkKodein.module(vertx))
            import(AppModule.module())
            import(overrides, allowOverride = true)
        }

        VertxkKodein.registerFactory(vertx, kodein)

        awaitResult<String> {
            VertxkKodein.deployVerticle<MainVerticle>(vertx, it)
        }

        return kodein
    }
}