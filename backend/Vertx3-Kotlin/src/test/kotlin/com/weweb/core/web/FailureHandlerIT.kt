package com.weweb.core.web

import com.weweb.BaseIT
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.stream.Collectors


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


    @Test
    fun shouldUseMultipleThreads() = runBlocking<Unit> {

        val messages = 100
        val count = CountDownLatch(messages)

        for (i in 0..messages) {
                    Thread({
                        val urlString = "http://127.0.0.1:${port()}/core/test/slow"
                        val url = URL(urlString)
                        val conn = url.openConnection()
                        val stream = conn.getInputStream()
                        read(stream)
                        stream.close()
                        count.countDown()
                    }).start()
                }
        count.await()
    }

    fun read(input: InputStream): String {
        val reader = BufferedReader(InputStreamReader(input))
        return reader.lines().collect(Collectors.joining("\n"))
    }

}