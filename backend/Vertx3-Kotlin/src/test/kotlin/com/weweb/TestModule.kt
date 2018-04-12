package com.weweb

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import com.weweb.core.CoreConfig
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.io.IOException
import java.net.ServerSocket


/**
 * Created by ufo on 18/07/17.
 */

class TestModule : VertxkModule() {

    override fun module() = Kodein.Module(allowSilentOverride = true) {
        bind<CoreConfig>() with singleton { CoreConfig(serverPort = getFreePort()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
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