package com.weweb

import com.ufoscout.vertxk.VertxK
import com.weweb.auth.AuthModule
import com.weweb.core.CoreModule
import com.weweb.core.config.CoreConfig
import com.weweb.core.json.JacksonMapperFactory
import io.vertx.config.ConfigRetriever
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.kodein.di.Kodein
import java.io.IOException


object AppMain {

    // initiate logging system
    private val log = LoggerFactory.getLogger(AppMain::class.java)

    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {
        runBlocking {
            start()
        }
    }

    suspend fun start(vararg modules: Kodein.Module): Kodein {

        println("Starting kotlin main")

        val vertx = Vertx.vertx()

        val deploymentOptions = DeploymentOptions()
        deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors())

        Json.mapper = JacksonMapperFactory.mapper
        Json.prettyMapper = JacksonMapperFactory.prettyMapper

        var retriever = ConfigRetriever.create(vertx)
        var config = awaitResult<JsonObject> { wait ->
            retriever.getConfig(wait);
        }

        return VertxK.start(
                vertx,
                deploymentOptions,
                AuthModule.module(),
                CoreModule.module(config.mapTo(CoreConfig::class.java)),
                *modules
        )

    }

}