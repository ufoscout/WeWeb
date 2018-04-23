package com.weweb.core.json

import com.weweb.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue

import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.LocalDate
import java.util.UUID
import org.junit.jupiter.api.Test

class JacksonJsonSerializerServiceTest : BaseTest() {

    private val jsonSerializerService = JacksonJsonSerializerService(JacksonMapperFactory.prettyMapper)

    @Test
    fun testJson() {
        val message = SerializerBean()
        message.id = SecureRandom().nextLong()
        message.name = UUID.randomUUID().toString()
        message.date = LocalDate.now()

        val json = jsonSerializerService.toJson(message)
        assertNotNull(json)
        assertTrue(json.contains("" + message.id))

        logger().info("JSON content: /n[{}]", json)

        val fromJson: SerializerBean = jsonSerializerService.fromJson(json)
        assertNotNull(fromJson)
        assertEquals(message.id, fromJson.id)
        assertEquals(message.date, fromJson.date)
        assertEquals(message.name, fromJson.name)

    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun testJsonOutputStream() {
        val message = SerializerBean()
        message.id = SecureRandom().nextLong()
        message.name = UUID.randomUUID().toString()
        message.date = LocalDate.now()

        val baos = ByteArrayOutputStream()

        jsonSerializerService.toPrettyPrintedJson(message, baos)

        val json = baos.toString(StandardCharsets.UTF_8.name())

        assertNotNull(json)
        assertTrue(json.contains("" + message.id))

        logger().info("JSON content: /n[{}]", json)

        val fromJson: SerializerBean = jsonSerializerService.fromJson(json)
        assertNotNull(fromJson)
        assertEquals(message.id, fromJson.id)
        assertEquals(message.date, fromJson.date)
        assertEquals(message.name, fromJson.name)

    }

}
