package com.ufoscout.vertxk

import io.vertx.ext.web.Router

interface RouterService {

    fun router(): Router

    suspend fun start(router: Router)

}