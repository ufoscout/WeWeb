package com.weweb.auth.web

import com.ufoscout.vertxk.kodein.VertxKComponent
import com.weweb.auth.service.AuthContextService
import io.vertx.ext.web.Router

class TestAuthenticationController(val router: Router,
                                   val auth: AuthContextService): VertxKComponent {

    override suspend fun start() {

        router.get("/test/public").handler {
            val authContext = auth.get(it.request())
            it.request().response().endWithJson(authContext.user)
        }

        router.get("/test/authenticated").handler {
            val authContext = auth.get(it).isAuthenticated()
            it.request().response().endWithJson(authContext.user)
        }

        router.get("/test/protected").handler {
            val authContext = auth.get(it).hasRole("ADMIN")
            it.request().response().endWithJson(authContext.user)
        }

    }

}