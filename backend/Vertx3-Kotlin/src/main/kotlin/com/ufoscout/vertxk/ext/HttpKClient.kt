package com.ufoscout.vertxk.ext

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitEvent
import kotlin.reflect.KClass

interface HttpKClient {

    suspend fun HttpClient.get(port: Int, host: String, requestUri: String): Buffer {
        return awaitEvent<Buffer> {
            getNow(port, host, requestUri) { response ->
                response.bodyHandler(it)
            }
        }
    }

    suspend fun HttpClient.postJson(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): Buffer {
        return awaitEvent<Buffer> {
            val request = post(port, host, requestUri, { response ->
                response.bodyHandler(it)
            })
            request.putHeader("content-type", "application/json")
            headers.forEach { request.putHeader(it.first, it.second) }
            request.setChunked(true)
            request.write(JsonObject.mapFrom(body).encode())
            request.end()
        }
    }

    suspend fun <T : Any> HttpClient.postJson(port: Int, host: String, requestUri: String, body: Any, kClass: KClass<T>, vararg headers: Pair<String, String>): T {
        val buffer = postJson(port, host, requestUri, body, *headers)
        return buffer.toJsonObject().mapTo(kClass.javaObjectType)
    }

}