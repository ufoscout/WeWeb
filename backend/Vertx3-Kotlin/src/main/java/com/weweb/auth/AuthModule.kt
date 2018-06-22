package com.weweb.auth

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.weweb.auth.service.InMemoryRolesProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AuthModule(): VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<RolesProvider>() with singleton { InMemoryRolesProvider() }
    }

    override suspend fun onInit(vertk: Vertk, kodein: Kodein) {
    }

}