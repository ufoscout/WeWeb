package io.vertx.starter

import com.github.salomonbrys.kodein.instance
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainVerticleTest {

    private var vertx: Vertx? = null
    private var port: Int? = 8080

    @Before
    fun setUp() = runBlocking<Unit> {
        var kodein = Main.launch(TestModule.module)
        var conf: AppConfig = kodein.instance();
        port = conf.serverPort()
        vertx = kodein.instance()

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
        Assert.assertEquals(MainVerticle.HELLO_MSG, body.toString())
    }

}