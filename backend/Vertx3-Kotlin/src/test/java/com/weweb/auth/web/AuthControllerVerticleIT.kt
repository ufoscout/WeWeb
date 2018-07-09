package com.weweb.auth.web

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.kodein.auth.AuthContextService
import com.ufoscout.vertk.kodein.web.ErrorDetails
import com.weweb.BaseIT
import com.weweb.auth.config.AuthContants
import com.weweb.auth.service.Roles
import com.weweb.auth.dto.CreateLoginDto
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import com.weweb.auth.dto.TokenResponseDto
import com.weweb.auth.service.UserService
import io.netty.handler.codec.http.HttpResponseStatus
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance
import java.util.*

class AuthControllerVerticleIT : BaseIT() {

    private val client = vertk().createHttpClient()
    private val jwt: JwtService = kodein().instance()
    private val authService: AuthContextService = kodein().instance()
    private val userService: UserService = kodein().instance()

    @Test
    fun shouldSuccessfulLoginWithValidCredentials() = runBlocking<Unit> {

        val loginDto = LoginDto("admin", "admin")

        val response = client.restPost<LoginResponseDto>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/login",
                loginDto)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)
        val responseDto = response.body
        assertNotNull(responseDto)
        assertNotNull(responseDto!!.token)
        val user = jwt.parse(responseDto.token, Auth::class)
        assertEquals("admin", user.username)
        assertEquals(1, user.roles.size)
        assertEquals(Roles.ADMIN, user.roles[0])
    }

    @Test
    fun shouldFailLoginWithWrongCredentials() = runBlocking {

        val loginDto = LoginDto("admin", UUID.randomUUID().toString())

        val response = client.restPost<ErrorDetails>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/login",
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
                AuthContants.BASE_AUTH_API + "/create",
                CreateLoginDto(username, password, password))
        assertEquals(HttpResponseStatus.OK.code(), responseCreate.statusCode)


        val responseLogin = client.restPost<LoginResponseDto>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/login",
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
                AuthContants.BASE_AUTH_API + "/create",
                CreateLoginDto(username, password, "anotherPassword"))
        assertEquals(422, responseCreate.statusCode)
        assertNotNull( responseCreate.body!!.details.get("confirmPassword") )

    }

    @Test
    fun createUserShouldFailIfNotUniqueUsername() = runBlocking<Unit> {

        val username = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()

        userService.createUser(CreateLoginDto(username, password, password))

        val responseCreate = client.restPost<ErrorDetails>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/create",
                CreateLoginDto(username, password, password))
        assertEquals(422, responseCreate.statusCode)
        assertNotNull( responseCreate.body!!.details.get("username") )

    }

    @Test
    fun shouldReturnCurrentAuthLinkedWithTheToken() = runBlocking<Unit> {

        val user = Auth(10, UUID.randomUUID().toString())
        val tokenString = authService.generateToken(user)
        val headers = Pair(com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER,
                "${com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER_SUFFIX}$tokenString")

        val response = client.restGet<LoginResponseDto>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/current",
                headers)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)
        val responseDto = response.body
        assertNotNull(responseDto)
        assertEquals(tokenString, responseDto!!.token)
        assertEquals(user.username, responseDto!!.auth.username)
    }

    @Test
    fun shouldReturnEmptyAuthIfBadToken() = runBlocking<Unit> {

        val headers = Pair(com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER,
                "${com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER_SUFFIX}${UUID.randomUUID().toString()}")

        val response = client.restGet<LoginResponseDto>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/current",
                headers)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)
        val responseDto = response.body
        assertNotNull(responseDto)
        assertEquals("", responseDto!!.token)
        assertEquals("", responseDto!!.auth.username)
    }

    @Test
    fun shouldReturnEmptyAuthIfExpiredToken() = runBlocking<Unit> {

        val user = Auth(10, UUID.randomUUID().toString())
        val tokenString = jwt.generate("", user, Date(System.currentTimeMillis() - 1000), Date(System.currentTimeMillis() - 1000))
        val headers = Pair(com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER,
                "${com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER_SUFFIX}$tokenString")

        val response = client.restGet<LoginResponseDto>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/current",
                headers)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)
        val responseDto = response.body
        assertNotNull(responseDto)
        assertEquals("", responseDto!!.token)
        assertEquals("", responseDto!!.auth.username)
    }

    @Test
    fun shouldReturnNewToken() = runBlocking<Unit> {

        val user = Auth(10, UUID.randomUUID().toString())
        val tokenString = jwt.generate("", user, Date(System.currentTimeMillis() - 1000), Date(System.currentTimeMillis() + 15000))
        val headers = Pair(com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER,
                "${com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER_SUFFIX}$tokenString")

        val response = client.restGet<TokenResponseDto>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/token/refresh",
                headers)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)
        val responseDto = response.body
        assertNotNull(responseDto)
        assertFalse(responseDto!!.token.isEmpty())
        assertNotEquals(tokenString, responseDto!!.token)

        assertEquals(user.username, jwt.parse<Auth>(responseDto!!.token).username)

    }

    @Test
    fun shouldNotRefreshTokenIfExpired() = runBlocking<Unit> {

        val user = Auth(10, UUID.randomUUID().toString())
        val tokenString = jwt.generate("", user, Date(System.currentTimeMillis() - 1000), Date(System.currentTimeMillis() - 1000))
        val headers = Pair(com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER,
                "${com.ufoscout.vertk.kodein.auth.AuthContants.JWT_TOKEN_HEADER_SUFFIX}$tokenString")

        val response = client.restGet<ErrorDetails>(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/token/refresh",
                headers)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)

    }
}