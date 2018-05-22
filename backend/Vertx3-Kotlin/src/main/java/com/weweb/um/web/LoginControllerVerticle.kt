package com.weweb.um.web

import com.ufoscout.vertxk.kodein.VertxKVerticle
import com.ufoscout.vertxk.kodein.auth.AuthContextService
import com.ufoscout.vertxk.kodein.auth.auth
import com.ufoscout.vertxk.kodein.router.RouterService
import com.weweb.um.config.UmContants
import com.weweb.um.dto.LoginDto
import com.weweb.um.dto.LoginResponseDto
import com.weweb.um.login.UserService

class LoginControllerVerticle (val routerService: RouterService,
                               val userService: UserService,
                               val auth: AuthContextService): VertxKVerticle() {

    override suspend fun start() {

        val router = routerService.router()

        router.restPost(UmContants.BASE_AUTH_API + "/login", LoginDto::class) { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            LoginResponseDto(token)
        }

        router.get(UmContants.BASE_AUTH_API + "/test/public").handler { rc ->
            val authContext = rc.auth()
            rc.request().response().endWithJson(authContext.auth)
        }

        router.get(UmContants.BASE_AUTH_API + "/test/authenticated").handler { rc ->
            val authContext = rc.auth().isAuthenticated()
            rc.request().response().endWithJson(authContext.auth)
        }

        router.get(UmContants.BASE_AUTH_API + "/test/protected").handler { rc ->
            val authContext = rc.auth().hasRole("ADMIN")
            rc.request().response().endWithJson(authContext.auth)
        }

    }

}