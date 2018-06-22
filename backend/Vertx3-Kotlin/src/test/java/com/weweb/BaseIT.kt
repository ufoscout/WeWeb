package com.weweb

import com.ufoscout.vertk.Vertk
import com.weweb.core.config.CoreConfig
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.kodein.di.DKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.IOException
import java.net.ServerSocket

abstract class BaseIT : BaseTest() {

    companion object {

        private var init = false
        private var vertk: Vertk? = null
        private var port: Int? = 8080
        private var kodein: DKodein? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {
            if (!init) {

                val serverPort = getFreePort().toString()
                System.setProperty("server.port", serverPort)

                val dk = AppMain.start(
                        TestKodeinModule()
                ).direct
                var conf: CoreConfig = dk.instance();
                port = conf.routerConfig.port
                vertk = dk.instance()
                kodein = dk
                init = true
            }
        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
         //   awaitResult<Void> { vertx!!.close(it) }
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

    protected fun port(): Int = port!!

    protected fun vertk(): Vertk = vertk!!

    protected fun kodein(): DKodein = kodein!!

}
