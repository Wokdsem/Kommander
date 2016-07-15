package com.wokdsem.kommander;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class KommanderTest {

	@Test
	public void publicMethods() throws Exception {
		Class<Kommander> kClass = Kommander.class;
		assertNotNull(kClass.getMethod("makeKommand", Action.class));
		assertNotNull(kClass.getMethod("cancelKommands", String.class));
	}

}
