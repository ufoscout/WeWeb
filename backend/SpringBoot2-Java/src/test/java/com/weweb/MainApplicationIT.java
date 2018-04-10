package com.weweb;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MainApplicationIT extends BaseIT {

	@Test
	public void contextShouldLoad() {
		assertNotNull(context);
	}

}
