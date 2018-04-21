package com.weweb.core.json

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.OutputStream
import kotlin.reflect.KClass


/**
 *
 * @author Francesco Cina'
 */
class JacksonJsonSerializerService @JvmOverloads constructor(val mapper: ObjectMapper) : JsonSerializerService {

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

    override fun <T: Any> fromJson(clazz: KClass<T>, json: String): T {
        try {
            return mapper.readValue(json, clazz.java)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

}
