package com.weweb.auth.web

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.vertk.kodein.auth.AuthContextService
import com.ufoscout.vertk.kodein.web.RouterService
import com.ufoscout.vertk.web.awaitRestGet
import com.ufoscout.vertk.web.awaitRestPost
import com.weweb.auth.config.AuthContants
import com.weweb.auth.dto.CreateLoginDto
import com.weweb.auth.dto.LoginDto
import com.weweb.auth.dto.LoginResponseDto
import com.weweb.auth.dto.TokenResponseDto
import com.weweb.auth.service.UserService
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthControllerVerticle (val routerService: RouterService,
                              val userService: UserService,
                              val auth: AuthContextService): CoroutineVerticle() {

    override suspend fun start() {

        val router = routerService.router()

        router.awaitRestPost<LoginDto>(AuthContants.BASE_AUTH_API + "/login") { rc, loginDto ->
            // println("Called Login ${loginDto.username} ${loginDto.password}")
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            // println("Token is ${token}")
            LoginResponseDto(token, login)
        }

        router.awaitRestPost<CreateLoginDto>(AuthContants.BASE_AUTH_API + "/create") { rc, dto ->
            val login = userService.createUser(dto)
            ""
        }

        router.awaitRestGet(AuthContants.BASE_AUTH_API + "/current") { rc ->
            try {
                val token = auth.tokenFrom(rc) ?: ""
                LoginResponseDto(token, auth.from(rc).auth)
            } catch (e: RuntimeException) {
                LoginResponseDto("", Auth())
            }
        }

        router.awaitRestGet(AuthContants.BASE_AUTH_API + "/token/refresh") { rc ->
            val token = auth.from(rc).isAuthenticated.auth
            TokenResponseDto(auth.generateToken(token))
        }

    }

}