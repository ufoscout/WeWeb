package io.vertx.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import io.vertx.starter.config.AppConfig
import java.io.IOException
import java.net.ServerSocket



/**
 * Created by ufo on 18/07/17.
 */

object TestModule {

    val module = Kodein.Module(allowSilentOverride = true){
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