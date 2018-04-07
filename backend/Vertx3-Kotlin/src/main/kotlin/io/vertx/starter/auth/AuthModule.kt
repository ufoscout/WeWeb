package io.vertx.starter.auth

import io.vertx.core.Vertx
import io.vertx.starter.vertxk.VertxkModule
import org.kodein.di.Kodein

object AuthModule : VertxkModule() {

    override fun module() = Kodein.Module {

    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {

    }

}