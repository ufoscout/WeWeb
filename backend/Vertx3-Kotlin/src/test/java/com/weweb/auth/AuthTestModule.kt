package com.weweb.auth

import com.ufoscout.vertxk.kodein.VertxKModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein

class AuthTestModule(): VertxKModule {

    override fun module() = Kodein.Module(allowSilentOverride = true) {
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}