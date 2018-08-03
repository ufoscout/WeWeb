package com.weweb

import com.ufoscout.properlty.Properlty
import com.ufoscout.properlty.reader.EnvironmentVariablesReader
import com.ufoscout.properlty.reader.SystemPropertiesReader
import com.ufoscout.properlty.reader.decorator.ToLowerCaseAndDotKeyReader
import com.ufoscout.vertk.kodein.VertkKodein
import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.ufoscout.vertk.kodein.json.JsonModule
import com.ufoscout.vertk.kodein.web.RouterModule
import com.weweb.auth.AuthModule
import com.weweb.core.CoreModule
import com.weweb.core.config.CoreConfig
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory
import kotlinx.coroutines.experimental.runBlocking
import org.kodein.di.Kodein
import java.io.IOException

object AppMain {

    init {
        System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
                SLF4JLogDelegateFactory::class.qualifiedName)
    }

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

    suspend fun start(vararg modules: VertkKodeinModule): Kodein {

        log.info("Starting kotlin main")

        val vertx = Vertx.vertx()

        val properlty = Properlty.builder()
                .caseSensitive(false)
                .add("classpath:conf/config.properties")
                .add(resourcePath = "classpath:conf/test-config.properties", ignoreNotFound = true)
                .add(SystemPropertiesReader())
                .add(EnvironmentVariablesReader().replace("_", "."))
                .build()

        val coreConfig = CoreConfig.build(properlty)

        val deploymentOptions = DeploymentOptions()
        deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors())

        return VertkKodein.start(
                vertx,
                CoreModule(coreConfig),
                AuthModule(deploymentOptions),
                JsonModule(),
                com.ufoscout.vertk.kodein.auth.AuthModule(coreConfig.jwt),
                RouterModule(coreConfig.router, HttpServerOptions()),
                *modules
        )

    }

}