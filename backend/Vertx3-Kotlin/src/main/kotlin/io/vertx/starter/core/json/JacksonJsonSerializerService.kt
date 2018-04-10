package io.vertx.starter.core.json

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
        mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(Jdk8Module())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans)
    }

    override fun toJson(`object`: Any): String {
        try {
            return mapper.writeValueAsString(`object`)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toJson(`object`: Any, out: OutputStream) {
        try {
            mapper.writeValue(out, `object`)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toPrettyPrintedJson(`object`: Any): String {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(`object`)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toPrettyPrintedJson(`object`: Any, out: OutputStream) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, `object`)
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
