package com.ufoscout.vertxk

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import org.kodein.di.Kodein

object VertxK {

    val VERTXK_DATA_MAP = "VERTXK_DATA_MAP"
    val VERTXK_DATA_CONFIG = VERTXK_DATA_MAP + "_CONFIG"

    suspend fun start(vertx: Vertx,
                      deploymentOptions: DeploymentOptions,
                      vararg modules: Kodein.Module): Kodein {
        val localMap = vertx.sharedData().getLocalMap<String, VertxKConfig>(VERTXK_DATA_MAP)
        val config = VertxKConfig(arrayOf(*modules))
        localMap.set(VERTXK_DATA_CONFIG, config)

        awaitResult<String> {
            vertx.deployVerticle(VertxKVerticle::class.java, deploymentOptions, it)
        }

        return config.kodein!!

    }

}