package com.ufoscout.vertxk.ext

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitEvent
import kotlin.reflect.KClass

interface HttpKClient {

    suspend fun HttpClient.getJson(port: Int, host: String, requestUri: String, vararg headers: Pair<String, String>): HttpKClientResponse<Buffer> {
        return awaitEvent<HttpKClientResponse<Buffer>> {
            val request = get(port, host, requestUri) { response ->
                response.bodyHandler({body ->
                    it.handle(HttpKClientResponse(response.statusCode(), body))
                })
            }
            headers.forEach { request.putHeader(it.first, it.second) }
            request.end()
        }
    }

    suspend fun <T : Any> HttpClient.getJson(port: Int, host: String, requestUri: String, kClass: KClass<T>, vararg headers: Pair<String, String>): HttpKClientResponse<T> {
        val response = getJson(port, host, requestUri, *headers)
        return HttpKClientResponse( response.statusCode, response.body.toJsonObject().mapTo(kClass.javaObjectType))
    }

    suspend fun HttpClient.postJson(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): HttpKClientResponse<Buffer> {
        return awaitEvent<HttpKClientResponse<Buffer>> {
            val request = post(port, host, requestUri, { response ->
                response.bodyHandler({body ->
                    it.handle(HttpKClientResponse(response.statusCode(), body))
                })
            })
            request.putHeader("content-type", "application/json")
            headers.forEach { request.putHeader(it.first, it.second) }
            request.setChunked(true)
            request.write(JsonObject.mapFrom(body).encode())
            request.end()
        }
    }

    suspend fun <T : Any> HttpClient.postJson(port: Int, host: String, requestUri: String, body: Any, kClass: KClass<T>, vararg headers: Pair<String, String>): HttpKClientResponse<T> {
        val response = postJson(port, host, requestUri, body, *headers)
        return HttpKClientResponse( response.statusCode, response.body.toJsonObject().mapTo(kClass.javaObjectType))
    }

}