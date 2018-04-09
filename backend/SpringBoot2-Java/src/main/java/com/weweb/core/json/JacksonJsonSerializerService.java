package com.weweb.core.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.OutputStream;


/**
 *
 * @author Francesco Cina'
 *
 */
public class JacksonJsonSerializerService implements JsonSerializerService {

	private final ObjectMapper mapper;

	public JacksonJsonSerializerService() {
		this(true, true);
	}

	public JacksonJsonSerializerService(final boolean failOnUnknownProperties) {
		this(failOnUnknownProperties, true);
	}

	public JacksonJsonSerializerService(final boolean failOnUnknownProperties, final boolean failOnEmptyBeans) {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans);
	}

	@Override
	public String toJson(final Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void toJson(final Object object, final OutputStream out) {
		try {
			mapper.writeValue(out, object);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toPrettyPrintedJson(final Object object) {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void toPrettyPrintedJson(final Object object, final OutputStream out) {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(out, object);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T fromJson(final Class<T> clazz, final String json) {
		try {
			return mapper.readValue(json, clazz);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

}
