package com.wokdsem.kommander;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

import static com.wokdsem.kommander.toolbox.Deliverers.getDefaultDeliverer;
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
	public void getInstance_negativePoolSize_exceptionThrown() {
		try {
			Kommander.getInstance(getDefaultDeliverer(), -1);
		} catch (Exception ignored) {
		}
	}

	@Test
	public void makeKommand_voidAction_actionExecuted() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		Kommander kommander = Kommander.getInstance(getDefaultDeliverer(), 2);
		kommander.makeKommand(new Action<Void>() {
			@Override
			public Void act() throws Throwable {
				latch.countDown();
				return null;
			}
		}).kommand();
		boolean countReleased = latch.await(1_000, TimeUnit.MICROSECONDS);
		assertThat(countReleased, is(true));
	}

}