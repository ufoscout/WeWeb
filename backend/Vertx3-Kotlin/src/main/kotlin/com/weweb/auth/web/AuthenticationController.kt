package com.weweb.auth.web

import com.weweb.auth.service.UserService
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthenticationController (router: Router, val userService: UserService): CoroutineVerticle() {

    override suspend fun start() {
        
    }

}