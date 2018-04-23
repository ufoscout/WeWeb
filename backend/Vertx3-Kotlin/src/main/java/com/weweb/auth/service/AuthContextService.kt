package com.weweb.auth.service

import com.weweb.auth.context.AuthContext
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext

interface AuthContextService {

    fun get(routingContext: RoutingContext): AuthContext {
        return get(routingContext.request())
    }

    fun get(httpServerRequest: HttpServerRequest): AuthContext

}