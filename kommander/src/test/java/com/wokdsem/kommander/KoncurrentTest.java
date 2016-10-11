package com.wokdsem.kommander;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class KoncurrentTest {

	@Test
	public void publicMethods() throws Exception {
		Class<Koncurrent> kClass = Koncurrent.class;
		assertNotNull(kClass.getMethod("kommand", Action[].class));
	}

}
