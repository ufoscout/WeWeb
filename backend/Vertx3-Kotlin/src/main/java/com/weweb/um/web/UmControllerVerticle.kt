package com.weweb.um.web

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.vertk.kodein.auth.AuthContextService
import com.ufoscout.vertk.kodein.web.RouterService
import com.weweb.um.config.UmContants
import com.weweb.um.dto.CreateUserDto
import com.weweb.um.dto.LoginDto
import com.weweb.um.dto.LoginResponseDto
import com.weweb.um.dto.TokenResponseDto
import com.weweb.um.service.UserService
import io.vertx.kotlin.coroutines.CoroutineVerticle

class UmControllerVerticle (val routerService: RouterService,
                            val userService: UserService,
                            val auth: AuthContextService): CoroutineVerticle() {

    override suspend fun start() {

        val router = routerService.router()

        router.restPost<LoginDto>(UmContants.BASE_UM_API + "/login") { rc, loginDto ->
            // println("Called Login ${loginDto.username} ${loginDto.password}")
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            // println("Token is ${token}")
            LoginResponseDto(token, login)
        }

        router.restPost<CreateUserDto>(UmContants.BASE_UM_API + "/create") { rc, dto ->
            val login = userService.createUser(dto)
            ""
        }

        router.restGet(UmContants.BASE_UM_API + "/current") { rc ->
            try {
                val token = auth.tokenFrom(rc) ?: ""
                LoginResponseDto(token, auth.from(rc).auth)
            } catch (e: RuntimeException) {
                LoginResponseDto("", Auth())
            }
        }

        router.restGet(UmContants.BASE_UM_API + "/token/refresh") { rc ->
            val token = auth.from(rc).isAuthenticated.auth
            TokenResponseDto(auth.generateToken(token))
        }

    }

}