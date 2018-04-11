package io.vertx.starter

import com.ufoscout.vertxk.Vertxk
import com.ufoscout.vertxk.VertxkModule
import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.starter.core.CoreModule
import io.vertx.starter.monitoring.MonitoringModule
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

    suspend fun start(vararg modules: VertxkModule): Kodein {

        println("Starting kotlin main")

        val vertx = Vertx.vertx()

        var retriever = ConfigRetriever.create(vertx)
        var config = awaitResult<JsonObject> { wait ->
            retriever.getConfig(wait);
        }

        return Vertxk.launch(vertx,
                MonitoringModule(),
                CoreModule(config),
                *modules
        );

    }

}