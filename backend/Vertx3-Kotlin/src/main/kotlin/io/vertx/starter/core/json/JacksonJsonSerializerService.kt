package io.vertx.starter.core.json

import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.IOException
import java.io.OutputStream


/**
 *
 * @author Francesco Cina'
 */
class JacksonJsonSerializerService @JvmOverloads constructor(failOnUnknownProperties: Boolean = false, failOnEmptyBeans: Boolean = false) : JsonSerializerService {

    private val mapper: ObjectMapper

    init {
        mapper = jacksonObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(Jdk8Module())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans)
    }

    override fun toJson(obj: Any): String {
        try {
            return mapper.writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toJson(obj: Any, out: OutputStream) {
        try {
            mapper.writeValue(out, obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toPrettyPrintedJson(obj: Any): String {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toPrettyPrintedJson(obj: Any, out: OutputStream) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun <T> fromJson(clazz: Class<T>, json: String): T {
        try {
            return mapper.readValue(json, clazz)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

}
