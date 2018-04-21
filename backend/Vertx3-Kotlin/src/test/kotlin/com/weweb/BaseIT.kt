package com.weweb

import com.ufoscout.vertxk.ext.Kx
import com.weweb.auth.AuthTestModule
import com.weweb.core.CoreTestModule
import com.weweb.core.config.CoreConfig
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.kodein.di.DKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

abstract class BaseIT : BaseTest(), Kx {


    companion object {
        private var vertx: Vertx? = null
        private var port: Int? = 8080
        private var kodein: DKodein? = null

        @BeforeClass @JvmStatic
        fun setUp() = runBlocking<Unit> {
            val dk = AppMain.start(
                    CoreTestModule.module(),
                    AuthTestModule.module()
            ).direct
            var conf: CoreConfig = dk.instance();
            port = conf.server.port
            vertx = dk.instance()
            kodein = dk
        }

        @AfterClass @JvmStatic
        fun tearDown() = runBlocking<Unit> {
            awaitResult<Void> { vertx!!.close(it) }
        }
    }


    protected fun port(): Int = port!!

    protected fun vertx(): Vertx = vertx!!

    protected fun kodein(): DKodein = kodein!!

}
