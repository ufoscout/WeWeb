package com.weweb.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.weweb.BaseTest;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.Test;

public class JacksonJsonSerializerServiceTest extends BaseTest {

	private final JsonSerializerService jsonSerializerService = new JacksonJsonSerializerService();

	@Test
	public void testJson() {
		final SerializerBean message = new SerializerBean();
		message.setId(new SecureRandom().nextLong());
		message.setName(UUID.randomUUID().toString());
		message.setDate(LocalDate.now());

		final String json = jsonSerializerService.toJson(message);
		assertNotNull(json);
		assertTrue(json.contains( "" + message.getId() ));

		getLogger().info("JSON content: /n[{}]", json);

		final SerializerBean fromJson = jsonSerializerService.fromJson(SerializerBean.class, json);
		assertNotNull(fromJson);
		assertEquals( message.getId(), fromJson.getId() );
		assertEquals( message.getDate(), fromJson.getDate() );
		assertEquals( message.getName(), fromJson.getName() );

	}

	@Test
	public void testJsonOutputStream() throws UnsupportedEncodingException {
		final SerializerBean message = new SerializerBean();
		message.setId(new SecureRandom().nextLong());
		message.setName(UUID.randomUUID().toString());
		message.setDate(LocalDate.now());

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		jsonSerializerService.toPrettyPrintedJson(message, baos);

		final String json = baos.toString(StandardCharsets.UTF_8.name());

		assertNotNull(json);
		assertTrue(json.contains( "" + message.getId() ));

		getLogger().info("JSON content: /n[{}]", json);

		final SerializerBean fromJson = jsonSerializerService.fromJson(SerializerBean.class, json);
		assertNotNull(fromJson);
		assertEquals( message.getId(), fromJson.getId() );
		assertEquals( message.getDate(), fromJson.getDate() );
		assertEquals( message.getName(), fromJson.getName() );

	}

}
