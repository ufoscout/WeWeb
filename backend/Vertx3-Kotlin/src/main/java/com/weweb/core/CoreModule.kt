package com.weweb.core

import com.ufoscout.coreutils.validation.SimpleValidatorService
import com.ufoscout.coreutils.validation.ValidatorService
import com.ufoscout.vertk.kodein.VertkKodeinModule
import com.weweb.core.config.CoreConfig
import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CoreModule(val config: CoreConfig) : VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<CoreConfig>() with singleton { config }
        bind<ValidatorService>() with singleton { SimpleValidatorService() }

        bind<WebClient>() with singleton { WebClient.create( instance(), WebClientOptions()
                .setMaxPoolSize(config.httpClient.maxPoolSize)
                .setConnectTimeout(config.httpClient.connectTimeoutSeconds * 1000)
                .setIdleTimeout(config.httpClient.idleTimeoutSeconds * 1000) )}
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}
