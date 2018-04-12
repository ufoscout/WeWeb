package com.weweb.monitoring

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import io.vertx.starter.HelloControllerVerticle
import org.kodein.di.Kodein

class MonitoringModule : VertxkModule() {

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        deployVerticle<HelloControllerVerticle>(vertx)
    }

}