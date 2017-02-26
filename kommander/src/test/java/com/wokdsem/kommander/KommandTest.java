package com.wokdsem.kommander;

import com.wokdsem.kommander.toolbox.Deliverers;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KommandTest {

	private final Deliverer deliverer;
	private final Executor executor;

	public KommandTest() {
		this.deliverer = Deliverers.getDefaultDeliverer();
		this.executor = new Executor(2);
	}

	@Test
	public void setOnCompleted_integerAction_integerReceivedOnCompleted() throws InterruptedException {
		final int valueAction = 5;
		final CountDownLatch latch = new CountDownLatch(1);
		Kommand<Integer> kommand = new Kommand<>(new Action<Integer>() {
			@Override
			public Integer action() throws Throwable {
				return valueAction;
			}
		}, deliverer, executor).setOnCompleted(new Response.OnCompleted<Integer>() {
			@Override
			public void onCompleted(Integer response) {
				assertThat(response, is(valueAction));
				latch.countDown();
			}
		});
		kommand.kommand();
		latch.await();
	}

	@Test
	public void setOnError_exceptionAction_exceptionReceivedOnError() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		Kommand<Void> kommand = new Kommand<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				throw new NullPointerException();
			}
		}, deliverer, executor).setOnError(new Response.OnError() {
			@Override
			public void onError(Throwable e) {
				latch.countDown();
			}
		});
		kommand.kommand();
		boolean released = latch.await(1_000, TimeUnit.MILLISECONDS);
		assertThat(released, is(true));
	}

	@Test
	public void kommand_cancelKommand_kommandIsCancelled() throws InterruptedException {
		final CountDownLatch inputLatch = new CountDownLatch(1);
		final CountDownLatch outputLatch = new CountDownLatch(1);
		Kommand<Void> kommand = new Kommand<>(new Action<Void>() {
			@Override
			public synchronized Void action() throws Throwable {
				try {
					inputLatch.countDown();
					wait();
				} catch (InterruptedException e) {
					outputLatch.countDown();
				}
				return null;
			}
		}, deliverer, executor);
		KommandToken token = kommand.kommand();
		inputLatch.await();
		token.cancel();
		boolean released = outputLatch.await(1_000, TimeUnit.MILLISECONDS);
		assertThat(released, is(true));
	}

	@Test
	public void kommandWithTokenBox_cancelAll_kommandIsCancelled() throws InterruptedException {
		KommandTokenBox tokenBox = new KommandTokenBox();
		final CountDownLatch inputLatch = new CountDownLatch(1);
		final CountDownLatch outputLatch = new CountDownLatch(1);
		Kommand<Void> kommand = new Kommand<>(new Action<Void>() {
			@Override
			public synchronized Void action() throws Throwable {
				try {
					inputLatch.countDown();
					wait();
				} catch (InterruptedException e) {
					outputLatch.countDown();
				}
				return null;
			}
		}, deliverer, executor);
		kommand.kommand(tokenBox);
		inputLatch.await();
		tokenBox.cancelAll();
		boolean released = outputLatch.await(1_000, TimeUnit.MILLISECONDS);
		assertThat(released, is(true));
	}

	@Test
	public void kommandWithTokenBoxTagged_cancelWithTag_kommandIsCancelled() throws InterruptedException {
		String tag = "TAG";
		KommandTokenBox tokenBox = new KommandTokenBox();
		final CountDownLatch inputLatch = new CountDownLatch(1);
		final CountDownLatch outputLatch = new CountDownLatch(1);
		Kommand<Void> kommand = new Kommand<>(new Action<Void>() {
			@Override
			public synchronized Void action() throws Throwable {
				try {
					inputLatch.countDown();
					wait();
				} catch (InterruptedException e) {
					outputLatch.countDown();
				}
				return null;
			}
		}, deliverer, executor);
		kommand.kommand(tokenBox, tag);
		inputLatch.await();
		tokenBox.cancel(tag);
		boolean released = outputLatch.await(1_000, TimeUnit.MILLISECONDS);
		assertThat(released, is(true));
	}

}