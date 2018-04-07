package io.vertx.starter.monitoring

import io.vertx.core.Vertx
import io.vertx.starter.HelloControllerVerticle
import io.vertx.starter.vertxk.VertxkModule
import org.kodein.di.Kodein

class MonitoringModule : VertxkModule() {

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        deployVerticle<HelloControllerVerticle>(vertx)
    }

}