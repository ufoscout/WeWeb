package com.weweb.core

import com.ufoscout.vertxk.VertxkModule
import com.weweb.core.web.TestWebController
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.IOException
import java.net.ServerSocket


/**
 * Created by ufo on 18/07/17.
 */

class CoreTestModule : VertxkModule() {

    override fun module() = Kodein.Module(allowSilentOverride = true) {
        bind<CoreConfig>() with singleton { CoreConfig(serverPort = getFreePort()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: DKodein) {
        val deploymentOptions = kodein.instance<DeploymentOptions>()
        deployVerticle<TestWebController>(vertx, deploymentOptions)
    }

    @Synchronized private fun getFreePort(): Int {
        try {
            ServerSocket(0).use { socket ->
                socket.reuseAddress = true
                return socket.localPort
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

}