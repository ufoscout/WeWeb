package com.weweb.core.service

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

interface RouterService {

    suspend fun router(vertx: Vertx): Router

}