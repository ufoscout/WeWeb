package com.weweb.core

import com.ufoscout.vertxk.RouterService
import com.weweb.core.config.CoreConfig
import com.weweb.core.exception.WebExceptionService
import com.weweb.core.exception.WebExceptionServiceImpl
import com.weweb.core.json.JacksonJsonSerializerService
import com.weweb.core.json.JacksonMapperFactory
import com.weweb.core.json.JsonSerializerService
import com.weweb.core.jwt.JwtService
import com.weweb.core.jwt.JwtServiceJJWT
import com.weweb.core.service.RouterServiceImpl
import io.vertx.core.DeploymentOptions
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object CoreModule {

    fun module(config: CoreConfig) = Kodein.Module {
            bind<CoreConfig>() with singleton { config }
            bind<WebExceptionService>() with singleton { WebExceptionServiceImpl() }
            bind<RouterService>() with singleton { RouterServiceImpl(instance(), instance(), instance()) }
            bind<JwtService>() with singleton {JwtServiceJJWT(config.jwt, instance())}
            bind<JsonSerializerService>() with singleton {JacksonJsonSerializerService(JacksonMapperFactory.mapper)}
            bind<DeploymentOptions>() with provider {
                val options = DeploymentOptions()
                options.instances = Runtime.getRuntime().availableProcessors()
                options
            }
        }

}