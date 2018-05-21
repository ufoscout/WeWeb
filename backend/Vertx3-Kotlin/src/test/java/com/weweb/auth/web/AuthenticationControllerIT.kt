package com.weweb.auth.web

import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertxk.kodein.router.ErrorDetails
import com.weweb.BaseIT
import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.UserContext
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import io.netty.handler.codec.http.HttpResponseStatus
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance
import java.util.*


class AuthenticationControllerIT : BaseIT() {

    val client = vertx().createHttpClient()
    val jwt: JwtService = kodein().instance()

    @Test
    fun shouldCallLogin() = runBlocking<Unit> {

        val loginDto = LoginDto("user", "user")
        val response = client.restPost(port(), "localhost", AuthContants.BASE_AUTH_API + "/login", loginDto, LoginResponseDto::class)

        assertEquals(200, response.statusCode)
        logger().info("token is ${response.body!!.token}")

    }

    @Test
    fun shouldGetUnauthorizedWithAnonymousUser() = runBlocking<Unit> {
        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/authenticated",
                ErrorDetails::class)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("NotAuthenticated", response.body!!.message)
    }

    @Test

    fun shouldGetUnauthorizedWithAnonymousUserOnProtectedUri() = runBlocking<Unit> {
        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/protected",
                ErrorDetails::class)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("NotAuthenticated", response.body!!.message)
    }

    @Test
    fun shouldSuccessfulAccessAuthenticatedApiWithToken() = runBlocking<Unit> {

        val sentUserContext = UserContext(UUID.randomUUID().toString(), arrayOf("ADMIN", "OTHER"))

        val token = jwt.generate(sentUserContext)

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/authenticated",
                UserContext::class,
                headers)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)

        val receivedUserContext = response.body
        assertNotNull(receivedUserContext)
        assertEquals(sentUserContext.username, receivedUserContext!!.username)
    }


    @Test
    fun shouldAccessPublicUriWithAnonymousUser() = runBlocking<Unit> {

        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/public",
                UserContext::class)

        val userContext = response.body
        assertNotNull(userContext)
        assertTrue(userContext!!.roles.size === 0)
        assertTrue(userContext!!.username.isEmpty())
    }


    @Test
    fun shouldSuccessfulLoginWithValidCredentials() = runBlocking<Unit> {

        val loginDto = LoginDto("user", "user")

        val response = client.restPost(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/login",
                loginDto,
                LoginResponseDto::class)


        val responseDto = response.body
        assertNotNull(responseDto)
        assertNotNull(responseDto!!.token)
        val userContext = jwt.parse(responseDto.token, UserContext::class)
        assertEquals("user", userContext.username)
        assertEquals(1, userContext.roles.size)
        assertEquals("USER", userContext.roles[0])
    }

    @Test
    @Throws(Exception::class)
    fun shouldFailLoginWithWrongCredentials() = runBlocking {

        val loginDto = LoginDto("user", UUID.randomUUID().toString())

        val response = client.restPost(
                port(),
                "localhost",
                AuthContants.BASE_AUTH_API + "/login",
                loginDto,
                ErrorDetails::class)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("BadCredentials", response.body!!.message)
    }


    @Test
    fun shouldNotAccessProtectedApiWithoutAdminRole() = runBlocking {

        val sentUserContext = UserContext(UUID.randomUUID().toString())

        val token = jwt.generate(sentUserContext)

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/protected",
                ErrorDetails::class,
                headers)

        assertEquals(HttpResponseStatus.FORBIDDEN.code(), response.statusCode)
        assertEquals("AccessDenied", response.body!!.message)

    }

    @Test
    fun shouldSuccessfulAccessProtectedApiWithAdminRole() = runBlocking {

        val sentUserContext = UserContext(UUID.randomUUID().toString(), arrayOf("ADMIN"))

        val token = jwt.generate(sentUserContext)

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/protected",
                UserContext::class,
                headers)

        val receivedUserContext = response.body
        assertNotNull(receivedUserContext)
        assertEquals(sentUserContext.username, receivedUserContext!!.username)
    }

    @Test
    fun shouldGetTokenExpiredExceptionIfTokenNotValid() = runBlocking {

        val sentUserContext = UserContext(UUID.randomUUID().toString())

        val token = jwt.generate("", sentUserContext, Date(System.currentTimeMillis() - 1000), Date(System.currentTimeMillis() - 1000))

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet(port(), "localhost",
                AuthContants.BASE_AUTH_API + "/test/protected",
                ErrorDetails::class,
                headers)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("TokenExpired", response.body!!.message)

    }

}
