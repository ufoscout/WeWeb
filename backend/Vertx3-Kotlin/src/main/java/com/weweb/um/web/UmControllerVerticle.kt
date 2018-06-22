package com.weweb.um.web

import com.ufoscout.vertk.kodein.auth.UserAuthService
import com.ufoscout.vertk.kodein.web.RouterService
import com.weweb.um.config.UmContants
import com.weweb.um.dto.CreateUserDto
import com.weweb.um.dto.LoginDto
import com.weweb.um.dto.LoginResponseDto
import com.weweb.um.service.UserService
import io.vertx.kotlin.coroutines.CoroutineVerticle

class UmControllerVerticle (val routerService: RouterService,
                            val userService: UserService,
                            val auth: UserAuthService): CoroutineVerticle() {

    override suspend fun start() {

        val router = routerService.router()

        router.restPost<LoginDto>(UmContants.BASE_UM_API + "/login") { rc, loginDto ->
            println("Called Login ${loginDto.username} ${loginDto.password}")
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            println("Token is ${token}")
            LoginResponseDto(token, login.username)
        }

        router.restPost<CreateUserDto>(UmContants.BASE_UM_API + "/create") { rc, dto ->
            val login = userService.createUser(dto)
            ""
        }

    }

}