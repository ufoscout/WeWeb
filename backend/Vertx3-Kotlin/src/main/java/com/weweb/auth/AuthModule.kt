package com.weweb.auth

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertxk.kodein.VertxKModule
import com.weweb.auth.service.InMemoryRolesProvider
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AuthModule(): VertxKModule {

    override fun module() = Kodein.Module {
        bind<RolesProvider>() with singleton { InMemoryRolesProvider() }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}