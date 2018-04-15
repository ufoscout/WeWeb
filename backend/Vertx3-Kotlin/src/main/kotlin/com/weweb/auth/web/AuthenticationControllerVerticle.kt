package com.weweb.auth.web

import com.weweb.auth.service.UserService
import com.weweb.core.service.RouterService
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthenticationControllerVerticle (val routerService: RouterService, val userService: UserService): CoroutineVerticle() {

    override suspend fun start() {
        
    }

}