package com.weweb.auth.web

import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertxk.kodein.VertxKVerticle
import com.ufoscout.vertxk.kodein.router.RouterService
import com.weweb.auth.config.AuthContants
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import com.weweb.auth.service.AuthContextService
import com.weweb.auth.service.UserService

class AuthenticationController (val routerService: RouterService,
                                val userService: UserService,
                                val jwt: JwtService,
                                val auth: AuthContextService): VertxKVerticle() {

    override suspend fun start() {

        val router = routerService.router()

        router.restPost(AuthContants.BASE_AUTH_API + "/login", LoginDto::class) { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val token = jwt.generate(login.username, login)
            println("Return token: [${token}]")
            LoginResponseDto(token)
        }

        router.get(AuthContants.BASE_AUTH_API + "/test/public").handler {
            val authContext = auth.get(it.request())
            it.request().response().endWithJson(authContext.auth)
        }

        router.get(AuthContants.BASE_AUTH_API + "/test/authenticated").handler {
            val authContext = auth.get(it).isAuthenticated()
            it.request().response().endWithJson(authContext.auth)
        }

        router.get(AuthContants.BASE_AUTH_API + "/test/protected").handler {
            val authContext = auth.get(it).hasRole("ADMIN")
            it.request().response().endWithJson(authContext.auth)
        }

    }

}