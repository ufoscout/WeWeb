package io.vertx.starter.vertxk

import com.ufoscout.vertxk.VertxkKodein
import io.vertx.core.AsyncResult
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import org.kodein.di.Kodein

abstract class VertxkModule {

    open fun module() = Kodein.Module {}

    abstract suspend fun onInit(vertx: Vertx, kodein: Kodein)

    /**
     * Deploy a [Verticle] in a [Vertx] instance using the default deployment options.
     *
     * @param vertx The [Vertx] instance where the [Verticle] is deployed.
     */
    inline suspend fun <reified T : Verticle> deployVerticle(vertx: Vertx) {
        awaitResult<String> {
            VertxkKodein.deployVerticle<T>(vertx, it)
        }
    }

    /**
     * Deploy a [Verticle] in a [Vertx] instance.
     *
     * @param vertx The [Vertx] instance where the [Verticle] is deployed.
     * @param options  the deployment options.
     */
    inline suspend fun <reified T : Verticle> deployVerticle(vertx: Vertx, options: DeploymentOptions) {
        awaitResult<String> {
            VertxkKodein.deployVerticle<T>(vertx, options, it)
        }
    }
}