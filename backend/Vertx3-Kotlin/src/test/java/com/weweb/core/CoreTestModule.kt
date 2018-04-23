package com.weweb.core

import com.weweb.core.web.TestWebController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import java.io.IOException
import java.net.ServerSocket


/**
 * Created by ufo on 18/07/17.
 */

object CoreTestModule {

    fun module() = Kodein.Module(allowSilentOverride = true) {
        bind<TestWebController>() with eagerSingleton { TestWebController(instance(), instance()) }
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