package com.weweb

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.awaitEvent
import io.vertx.kotlin.coroutines.awaitResult
import com.weweb.core.CoreConfig
import io.vertx.starter.HelloControllerVerticle
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kodein.di.direct
import org.kodein.di.generic.instance

class AppMainTest : BaseTest() {

    private var vertx: Vertx? = null
    private var port: Int? = 8080

    @Before
    fun setUp() = runBlocking<Unit> {
        var kodein = AppMain.start(TestModule())
        val dk = kodein.direct
        var conf: CoreConfig = dk.instance();
        port = conf.serverPort()
        vertx = dk.instance()

    }

    @After
    fun tearDown() = runBlocking<Unit> {
        awaitResult<Void> { vertx!!.close(it) }
    }

    @Test
    fun testThatTheServerIsStarted() = runBlocking<Unit> {

        val body = awaitEvent<Buffer> {
            vertx!!.createHttpClient().getNow(port!!, "localhost", "/") { response ->
                Assert.assertEquals(response.statusCode(), 200)
                response.bodyHandler(it)
            }
        }
        Assert.assertTrue(body.length() > 0)
        Assert.assertEquals(HelloControllerVerticle.HELLO_MSG, body.toString())
    }

}