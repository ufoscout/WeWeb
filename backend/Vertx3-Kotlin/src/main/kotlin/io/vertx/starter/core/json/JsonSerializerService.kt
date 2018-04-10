package io.vertx.starter.core.json

import java.io.OutputStream

/**
 *
 * @author Francesco Cina'
 */
interface JsonSerializerService {

    /**
     * Return the json representation of the Bean
     * @param object
     * @return
     */
    fun toJson(`object`: Any): String

    /**
     * Return the json representation of the Bean
     * @param object
     */
    fun toJson(`object`: Any, out: OutputStream)

    /**
     * Return the json representation of the Bean
     * WARN: it is slower than the other method!
     * @param object
     * @return
     */
    fun toPrettyPrintedJson(`object`: Any): String

    /**
     * Return the json representation of the Bean
     * WARN: it is slower than the other method!
     * @param object
     */
    fun toPrettyPrintedJson(`object`: Any, out: OutputStream)

    fun <T> fromJson(clazz: Class<T>, json: String): T

}
