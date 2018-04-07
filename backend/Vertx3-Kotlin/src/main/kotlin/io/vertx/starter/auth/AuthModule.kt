package io.vertx.starter.auth

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein

object AuthModule : VertxkModule() {

    override fun module() = Kodein.Module {

    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {

    }

}