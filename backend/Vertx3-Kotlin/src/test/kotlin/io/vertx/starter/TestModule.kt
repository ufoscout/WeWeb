package io.vertx.starter

import io.vertx.starter.config.AppConfig
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.io.IOException
import java.net.ServerSocket



/**
 * Created by ufo on 18/07/17.
 */

object TestModule {

    val module = Module(allowSilentOverride = true){
        bind<AppConfig>() with singleton { AppConfig(serverPort = getFreePort()) }
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