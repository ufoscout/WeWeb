package com.weweb.auth.web

import com.weweb.BaseIT
import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.UserContext
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test


class AuthenticationControllerIT : BaseIT() {

    @Test
    fun shouldCallLogin() = runBlocking<Unit> {

        val client = vertx().createHttpClient()

        val loginDto = LoginDto("user", "user")
        val buffer = client.postJson(port(), "localhost", AuthContants.BASE_AUTH_API + "/login", loginDto, LoginResponseDto::class)

        println("After call")

        //assertTrue(body.length() > 0)
        logger().info("token is ${buffer}")

    }
}
