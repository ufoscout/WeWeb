package com.weweb.auth.web

import com.ufoscout.coreutils.jwt.TokenExpiredException
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertxk.kodein.VertxKVerticle
import com.ufoscout.vertxk.kodein.router.RouterService
import com.ufoscout.vertxk.kodein.router.WebException
import com.ufoscout.vertxk.kodein.router.WebExceptionService
import com.ufoscout.vertxk.kodein.router.registerTransformer
import com.weweb.auth.config.AuthContants
import com.weweb.auth.context.UserContext
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import com.weweb.auth.exception.BadCredentialsException
import com.weweb.auth.exception.UnauthenticatedException
import com.weweb.auth.exception.UnauthorizedException
import com.weweb.auth.service.AuthContextService
import com.weweb.auth.service.UserService

class AuthenticationController (val routerService: RouterService,
                                val userService: UserService,
                                val jwt: JwtService,
                                val auth: AuthContextService,
                                val webExceptionService: WebExceptionService): VertxKVerticle() {

    override suspend fun start() {

        webExceptionService.registerTransformer<BadCredentialsException>({exp -> WebException(code = 401, message = "BadCredentials") })
        webExceptionService.registerTransformer<UnauthenticatedException>({exp -> WebException(code = 401, message = "NotAuthenticated") })
        webExceptionService.registerTransformer<TokenExpiredException>({exp -> WebException(code = 401, message = "TokenExpired") })
        webExceptionService.registerTransformer<UnauthorizedException>({exp -> WebException(code = 403, message = "AccessDenied") })

        val router = routerService.router()

        router.restPost(AuthContants.BASE_AUTH_API + "/login", LoginDto::class) { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val roles = login.roles.toTypedArray()
            val token = jwt.generate(login.username, UserContext(login.username, roles))
            println("Return token: [${token}]")
            LoginResponseDto(token)
        }

        router.get(AuthContants.BASE_AUTH_API + "/test/public").handler {
            val authContext = auth.get(it.request())
            it.request().response().endWithJson(authContext.user)
        }

        router.get(AuthContants.BASE_AUTH_API + "/test/authenticated").handler {
            val authContext = auth.get(it).isAuthenticated()
            it.request().response().endWithJson(authContext.user)
        }

        router.get(AuthContants.BASE_AUTH_API + "/test/protected").handler {
            val authContext = auth.get(it).hasRole("ADMIN")
            it.request().response().endWithJson(authContext.user)
        }

    }

}