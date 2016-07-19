package com.wokdsem.kommander;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CentralKommanderTest {

	@Test
	public void publicMethods() throws Exception {
		Class<CentralKommander> cClass = CentralKommander.class;
		assertNotNull(cClass.getMethod("getInstance"));
		assertNotNull(cClass.getMethod("getInstance", KommanderConfig.class));
		assertNotNull(cClass.getMethod("buildKommander"));
		assertNotNull(cClass.getMethod("buildKommander", Deliverer.class));
		assertNotNull(cClass.getMethod("getKoncurrent"));
	}

}
