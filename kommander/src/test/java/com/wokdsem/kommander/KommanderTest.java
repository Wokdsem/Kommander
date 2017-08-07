package com.wokdsem.kommander;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class KommanderTest {
	
	@Test
	public void getInstance_nullDeliver_exceptionThrown() {
		try {
			Kommander.getInstance(null);
			fail();
		} catch (Exception ignored) {
		}
	}
	
	@Test
	public void getInstance_nullExecutor_exceptionThrown() {
		try {
			Kommander.getInstance(null);
			fail();
		} catch (Exception ignored) {
		}
	}
	
	@Test
	public void makeKommand_voidAction_actionExecuted() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		Kommander
			.getInstance()
			.makeKommand(new Action<Void>() {
				@Override
				public Void action() throws Throwable {
					latch.countDown();
					return null;
				}
			})
			.kommand();
		boolean countReleased = latch.await(1_000, TimeUnit.MILLISECONDS);
		assertThat(countReleased, is(true));
	}
	
}