package com.weweb.core.web

import com.weweb.BaseIT
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.*

class FailureHandlerIT: BaseIT() {

    @Test
    fun shouldThrow500() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()

        val body = awaitEvent<Buffer> {
            vertx().createHttpClient().getNow(port(), "localhost", "/core/test/fatal/${message}") { response ->
                Assert.assertEquals(response.statusCode(), 500)
                response.bodyHandler(it)
            }
        }

        Assert.assertTrue(body.length() > 0)
        logger().info("body is ${body}")
        Assert.assertTrue(body.toString().contains("Error code:"))
        Assert.assertFalse(body.toString().contains(message))
    }

    @Test
    fun shouldThrowWebException() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()
        val statusCode = 400 + Random().nextInt(50)

        val body = awaitEvent<Buffer> {
            vertx().createHttpClient().getNow(port(), "localhost", "/core/test/webException/${statusCode}/${message}") { response ->
                Assert.assertEquals(statusCode, response.statusCode())
                response.bodyHandler(it)
            }
        }

        Assert.assertTrue(body.length() > 0)
        logger().info("body is ${body}")
        Assert.assertEquals(message, body.toString())
    }

    @Test
    fun shouldMapWebExceptionFromCustomException() = runBlocking<Unit> {
        
        val body = awaitEvent<Buffer> {
            vertx().createHttpClient().getNow(port(), "localhost", "/core/test/customException") { response ->
                Assert.assertEquals(12345, response.statusCode())
                response.bodyHandler(it)
            }
        }

        Assert.assertTrue(body.length() > 0)
        logger().info("body is ${body}")
        Assert.assertEquals("CustomTestExceptionMessage", body.toString())
    }

}