package com.weweb.um.web

import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.kodein.auth.User
import com.ufoscout.vertk.kodein.auth.UserAuthService
import com.ufoscout.vertk.kodein.web.ErrorDetails
import com.weweb.BaseIT
import com.weweb.auth.service.Roles
import com.weweb.um.config.UmContants
import com.weweb.um.dto.CreateUserDto
import com.weweb.um.dto.LoginDto
import com.weweb.um.dto.LoginResponseDto
import com.weweb.um.service.UserService
import io.netty.handler.codec.http.HttpResponseStatus
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance
import java.util.*

class UmControllerVerticleIT : BaseIT() {

    val client = vertk().createHttpClient()
    val jwt: JwtService = kodein().instance()
    val authService: UserAuthService = kodein().instance()
    val userService: UserService = kodein().instance()

    @Test
    fun shouldSuccessfulLoginWithValidCredentials() = runBlocking<Unit> {

        val loginDto = LoginDto("admin", "admin")

        val response = client.restPost<LoginResponseDto>(
                port(),
                "localhost",
                UmContants.BASE_UM_API + "/login",
                loginDto)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)
        val responseDto = response.body
        assertNotNull(responseDto)
        assertNotNull(responseDto!!.token)
        val user = jwt.parse(responseDto.token, User::class)
        assertEquals("admin", user.username)
        assertEquals(1, authService.decode(user.roles).size)
        assertEquals(Roles.ADMIN, authService.decode(user.roles)[0].name)
    }

    @Test
    fun shouldFailLoginWithWrongCredentials() = runBlocking {

        val loginDto = LoginDto("admin", UUID.randomUUID().toString())

        val response = client.restPost<ErrorDetails>(
                port(),
                "localhost",
                UmContants.BASE_UM_API + "/login",
                loginDto)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("BadCredentials", response.body!!.message)
    }

    @Test
    fun shouldSuccessfulCreateNewUser() = runBlocking<Unit> {

        val username = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        val responseCreate = client.restPost<String>(
                port(),
                "localhost",
                UmContants.BASE_UM_API + "/create",
                CreateUserDto(username, password, password))
        assertEquals(HttpResponseStatus.OK.code(), responseCreate.statusCode)


        val responseLogin = client.restPost<LoginResponseDto>(
                port(),
                "localhost",
                UmContants.BASE_UM_API + "/login",
                LoginDto(username, password))
        assertEquals(HttpResponseStatus.OK.code(), responseLogin.statusCode)

    }

    @Test
    fun createUserShouldFailIfNotSamePasswords() = runBlocking<Unit> {

        val username = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        val responseCreate = client.restPost<ErrorDetails>(
                port(),
                "localhost",
                UmContants.BASE_UM_API + "/create",
                CreateUserDto(username, password, "anotherPassword"))
        assertEquals(422, responseCreate.statusCode)
        assertNotNull( responseCreate.body!!.details.get("confirmPassword") )

    }

    @Test
    fun createUserShouldFailIfNotUniqueUsername() = runBlocking<Unit> {

        val username = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        userService.createUser(CreateUserDto(username, password, password))

        val responseCreate = client.restPost<ErrorDetails>(
                port(),
                "localhost",
                UmContants.BASE_UM_API + "/create",
                CreateUserDto(username, password, password))
        assertEquals(422, responseCreate.statusCode)
        assertNotNull( responseCreate.body!!.details.get("username") )

    }
}
