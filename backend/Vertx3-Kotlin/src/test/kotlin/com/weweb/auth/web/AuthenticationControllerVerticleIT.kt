package com.weweb.auth.web

import com.weweb.BaseIT
import com.weweb.auth.config.AuthContants
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class AuthenticationControllerVerticleIT: BaseIT() {

    @Test
    fun shouldCallLogin() = runBlocking<Unit> {
    /*

        val body = awaitEvent<Buffer> {
            vertx().createHttpClient().getNow(port(), "localhost", AuthContants.BASE_AUTH_API + "/login") { response ->
                assertEquals(200, response.statusCode())
                response.bodyHandler(it)
            }
        }

        assertTrue(body.length() > 0)
        logger().info("body is ${body}")
*/
    }
}