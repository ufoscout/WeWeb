package com.ufoscout.vertxk.ext

import io.vertx.core.http.HttpClient
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitEvent
import kotlin.reflect.KClass

interface HttpClientExt {

    suspend fun <T : Any> HttpClient.restGet(port: Int, host: String, requestUri: String, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = get(port, host, requestUri) { response ->
                response.bodyHandler({body ->
                    it.handle(HttpClientKResponse(response.statusCode(), body.toJsonObject().mapTo(responseClass.javaObjectType)))
                })
            }
            headers.forEach { request.putHeader(it.first, it.second) }
            request.end()
        }
    }

    suspend fun <T : Any> HttpClient.restPost(port: Int, host: String, requestUri: String, body: Any, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = post(port, host, requestUri, { response ->
                response.bodyHandler({body ->
                    it.handle(HttpClientKResponse( response.statusCode(), body.toJsonObject().mapTo(responseClass.javaObjectType)))
                })
            })
            request.putHeader("content-type", "application/json")
            headers.forEach { request.putHeader(it.first, it.second) }
            request.setChunked(true)
            request.write(JsonObject.mapFrom(body).encode())
            request.end()
        }
    }

}
