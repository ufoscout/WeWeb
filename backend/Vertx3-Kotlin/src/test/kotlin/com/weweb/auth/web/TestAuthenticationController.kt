package com.weweb.auth.web

import com.ufoscout.vertxk.kodein.VertxKComponent
import com.weweb.auth.config.AuthContants
import com.weweb.auth.service.AuthContextService
import io.vertx.ext.web.Router

class TestAuthenticationController(val router: Router,
                                   val auth: AuthContextService): VertxKComponent {

    override suspend fun start() {

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