package com.weweb

import com.weweb.core.CoreConfig
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Before
import org.kodein.di.DKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

abstract class BaseIT : BaseTest() {

    private var vertx: Vertx? = null
    private var port: Int? = 8080
    private var kodein: DKodein? = null

    @Before
    fun setUp() = runBlocking<Unit> {
        val dk = AppMain.start(TestModule()).direct
        var conf: CoreConfig = dk.instance();
        port = conf.serverPort()
        vertx = dk.instance()
        kodein = dk
    }

    @After
    fun tearDown() = runBlocking<Unit> {
        awaitResult<Void> { vertx!!.close(it) }
    }

    protected fun port(): Int = port!!

    protected fun vertx(): Vertx = vertx!!

    protected fun kodein(): DKodein = kodein!!

}
