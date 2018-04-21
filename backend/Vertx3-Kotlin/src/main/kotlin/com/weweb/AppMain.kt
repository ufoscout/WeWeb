package com.weweb

import com.ufoscout.vertxk.Vertxk
import com.ufoscout.vertxk.VertxkModule
import com.ufoscout.vertxk.util.VertxkKodein
import com.weweb.auth.AuthModule
import com.weweb.core.json.JacksonMapperFactory
import com.weweb.core.CoreModule
import com.weweb.core.config.CoreConfig
import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.shareddata.SharedData
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule
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

/*
        val kodein = Kodein {
            bind<Vertx>() with singleton { vertx }
            bind<EventBus>() with singleton { vertx.eventBus() }
            bind<FileSystem>() with singleton { vertx.fileSystem() }
            bind<SharedData>() with singleton { vertx.sharedData() }
            import(AuthModule().module())
            import(CoreModule(config.mapTo(CoreConfig::class.java)).module())
            import(modules[0].module())
        }



        return kodein.direct
*/


    }

}