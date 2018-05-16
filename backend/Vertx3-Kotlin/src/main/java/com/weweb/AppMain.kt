package com.weweb

import com.ufoscout.properlty.Properlty
import com.ufoscout.properlty.reader.EnvironmentVariablesReader
import com.ufoscout.properlty.reader.SystemPropertiesReader
import com.ufoscout.properlty.reader.decorator.ToLowerCaseAndDotKeyReader
import com.ufoscout.vertxk.kodein.VertxK
import com.weweb.auth.AuthModule
import com.weweb.core.CoreModule
import com.weweb.core.config.CoreConfig
import com.weweb.core.json.JacksonMapperFactory
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory
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
            try {
                start()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    suspend fun start(vararg modules: Kodein.Module): Kodein {

        log.info("Starting kotlin main")

        val vertx = Vertx.vertx()

        val deploymentOptions = DeploymentOptions()
        deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors())

        Json.mapper = JacksonMapperFactory.mapper
        Json.prettyMapper = JacksonMapperFactory.prettyMapper

        val properlty = Properlty.builder()
                .add("classpath:conf/config.properties")
                .add(resourcePath = "classpath:conf/test-config.properties", ignoreNotFound = true)
                .add(SystemPropertiesReader())
                .add(EnvironmentVariablesReader())
                .add(ToLowerCaseAndDotKeyReader(EnvironmentVariablesReader()))
                .build()

        return VertxK.start(
                vertx,
                deploymentOptions,
                AuthModule.module(),
                CoreModule.module(CoreConfig.build(properlty)),
                *modules
        )

    }

}