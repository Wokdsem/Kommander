package com.wokdsem.android.kommander;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class KommandTest {

	@Test
	public void publicMethods() throws Exception {
		Class<Kommand> kClass = Kommand.class;
		assertNotNull(kClass.getMethod("setOnCompleted", Response.OnCompleted.class));
		assertNotNull(kClass.getMethod("setOnError", Response.OnError.class));
		assertNotNull(kClass.getMethod("setTag", String.class));
		assertNotNull(kClass.getMethod("kommand"));
	}

}
