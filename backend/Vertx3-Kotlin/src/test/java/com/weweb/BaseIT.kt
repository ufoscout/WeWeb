package com.weweb

import com.ufoscout.vertxk.K
import com.weweb.auth.AuthTestModule
import com.weweb.core.CoreTestModule
import com.weweb.core.config.CoreConfig
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.kodein.di.DKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.IOException
import java.net.ServerSocket

abstract class BaseIT : BaseTest(), K {

    companion object {

        private var vertx: Vertx? = null
        private val port: Int = getFreePort()
        private var kodein: DKodein? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            System.setProperty("server.port", port.toString())

            val dk = AppMain.start(
                    CoreTestModule.module(),
                    AuthTestModule.module()
            ).direct
            var conf: CoreConfig = dk.instance();
            vertx = dk.instance()
            kodein = dk
        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            awaitResult<Void> { vertx!!.close(it) }
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

    protected fun port(): Int = port

    protected fun vertx(): Vertx = vertx!!

    protected fun kodein(): DKodein = kodein!!

}
