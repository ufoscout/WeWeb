package com.weweb.core.json;

import java.io.OutputStream;

/**
 * 
 * @author Francesco Cina'
 *
 */
public interface JsonSerializerService {

	/**
	 * Return the json representation of the Bean
	 * @param object
	 * @return
	 */
	String toJson(Object object);

	/**
	 * Return the json representation of the Bean
	 * @param object
	 */
	void toJson(Object object, OutputStream out);
	
	/**
	 * Return the json representation of the Bean
	 * WARN: it is slower than the other method!
	 * @param object
	 * @return
	 */
	String toPrettyPrintedJson(Object object);

	/**
	 * Return the json representation of the Bean
	 * WARN: it is slower than the other method!
	 * @param object
	 */
	void toPrettyPrintedJson(Object object, OutputStream out);
	
	<T> T fromJson(Class<T> clazz, String json);

}
