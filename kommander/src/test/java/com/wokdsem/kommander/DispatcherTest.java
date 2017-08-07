package com.wokdsem.kommander;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DispatcherTest {
	
	private final Dispatcher dispatcher;
	
	public DispatcherTest() {
		this.dispatcher = new Dispatcher(newSingleThreadExecutor());
	}
	
	@Test
	public void kommand_withNoDelay_directExecution() throws InterruptedException {
		assertTrue(kommand(0) < 5000);
	}
	
	@Test
	public void kommand_withDelay_actionDelayed() throws InterruptedException {
		assertTrue(kommand(500) >= 500);
	}
	
	private long kommand(long delay) throws InterruptedException {
		final long initialTime = System.currentTimeMillis();
		final AtomicLong spendTime = new AtomicLong();
		final CountDownLatch latch = new CountDownLatch(1);
		RunnableActionBundle<Void> bundle = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				spendTime.set(System.currentTimeMillis() - initialTime);
				latch.countDown();
				return null;
			}
		}).build();
		dispatcher.kommand(bundle, delay);
		latch.await();
		return spendTime.get();
	}
	
	@Test
	public void kommand_secondDelayAdvanceFirst_advanced() throws InterruptedException {
		final AtomicBoolean value = new AtomicBoolean(false);
		final CountDownLatch latch = new CountDownLatch(2);
		RunnableActionBundle<Void> firstBundle = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				value.set(true);
				latch.countDown();
				return null;
			}
		}).build();
		RunnableActionBundle<Void> secondBundle = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				value.set(false);
				latch.countDown();
				return null;
			}
		}).build();
		dispatcher.kommand(firstBundle, 500);
		dispatcher.kommand(secondBundle, 250);
		latch.await();
		assertTrue(value.get());
	}
	
	@Test
	public void kommand_cancelActionWithDelay_actionCanceled() throws InterruptedException {
		final AtomicBoolean value = new AtomicBoolean(false);
		final CountDownLatch latch = new CountDownLatch(1);
		RunnableActionBundle<Void> bundle = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					value.set(true);
				}
				latch.countDown();
				return null;
			}
		}).build();
		KommandToken token = dispatcher.kommand(bundle, 150);
		Thread.sleep(500);
		token.cancel();
		latch.await();
		assertThat(value.get(), is(true));
	}
	
}