package com.weweb

import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.awaitEvent
import io.vertx.starter.HelloControllerVerticle
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test

class AppMainIT : BaseIT() {

    @Test
    fun testThatTheServerIsStarted() = runBlocking<Unit> {

        val body = awaitEvent<Buffer> {
            vertx().createHttpClient().getNow(port(), "localhost", "/") { response ->
                Assert.assertEquals(response.statusCode(), 200)
                response.bodyHandler(it)
            }
        }

        Assert.assertTrue(body.length() > 0)
        Assert.assertEquals(HelloControllerVerticle.HELLO_MSG, body.toString())
    }

}