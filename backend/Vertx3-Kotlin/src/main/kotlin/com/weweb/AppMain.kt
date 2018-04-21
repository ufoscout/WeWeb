package com.weweb

import com.ufoscout.vertxk.Vertxk
import com.ufoscout.vertxk.VertxkModule
import com.weweb.auth.AuthModule
import com.weweb.core.json.JacksonMapperFactory
import com.weweb.core.CoreModule
import com.weweb.core.config.CoreConfig
import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.kodein.di.DKodein
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

    suspend fun start(vararg modules: VertxkModule): DKodein {

        println("Starting kotlin main")

        val vertx = Vertx.vertx()
        Json.mapper = JacksonMapperFactory.mapper
        Json.prettyMapper = JacksonMapperFactory.prettyMapper

        var retriever = ConfigRetriever.create(vertx)
        var config = awaitResult<JsonObject> { wait ->
            retriever.getConfig(wait);
        }

        return Vertxk.launch(vertx,
                AuthModule(),
                CoreModule(config.mapTo(CoreConfig::class.java)),
                *modules
        );

    }

}