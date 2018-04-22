package com.weweb.auth.web

import com.weweb.BaseIT
import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.UserContext
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class AuthenticationControllerIT : BaseIT() {

    @Test
    fun shouldCallLogin() = runBlocking<Unit> {

        val client = vertx().createHttpClient()

        val loginDto = LoginDto("user", "user")
        val response = client.restPost(port(), "localhost", AuthContants.BASE_AUTH_API + "/login", loginDto, LoginResponseDto::class)

        assertEquals(200, response.statusCode)
        logger().info("token is ${response.body.token}")

    }
}
