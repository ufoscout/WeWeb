package com.weweb.core.service

import com.ufoscout.vertxk.VertxKComponent
import io.vertx.ext.web.Router

interface RouterService: VertxKComponent {

    fun router(): Router

}