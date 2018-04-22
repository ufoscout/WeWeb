package com.ufoscout.vertxk.ext

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitEvent

class HttpClientK(val httpClient: HttpClient) {

    suspend fun getForBody(port: Int, host: String, requestUri: String, vararg headers: Pair<String, String>): HttpClientKResponse<Buffer> {
        return awaitEvent<HttpClientKResponse<Buffer>> {
            val request = httpClient.get(port, host, requestUri) { response ->
                response.bodyHandler({body ->
                    it.handle(HttpClientKResponse(response.statusCode(), body))
                })
            }
            headers.forEach { request.putHeader(it.first, it.second) }
            request.end()
        }
    }

    inline suspend fun <reified T : Any> getForRest(port: Int, host: String, requestUri: String, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        val response = getForBody(port, host, requestUri, *headers)
        val body = response.body
        return HttpClientKResponse( response.statusCode, body.toJsonObject().mapTo(T::class.java))
    }

    suspend fun postJson(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): HttpClientKResponse<Buffer> {
        return awaitEvent<HttpClientKResponse<Buffer>> {
            val request = httpClient.post(port, host, requestUri, { response ->
                response.bodyHandler({body ->
                    it.handle(HttpClientKResponse(response.statusCode(), body))
                })
            })
            request.putHeader("content-type", "application/json")
            headers.forEach { request.putHeader(it.first, it.second) }
            request.setChunked(true)
            request.write(JsonObject.mapFrom(body).encode())
            request.end()
        }
    }

    inline suspend fun <reified T : Any> postForRest(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        val response = postJson(port, host, requestUri, body, *headers)
        val body = response.body
        return HttpClientKResponse( response.statusCode, body.toJsonObject().mapTo(T::class.java))
    }

}