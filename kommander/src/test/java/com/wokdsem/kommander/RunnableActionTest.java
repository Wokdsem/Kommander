package com.wokdsem.kommander;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.junit.Assert;
import org.junit.Test;

import static com.wokdsem.kommander.toolbox.Deliverers.getDefaultDeliverer;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RunnableActionTest {
	
	@Test
	public void run_normalExecutor_onResultCalled() {
		final int value = 5;
		RunnableActionBundle.Builder<Integer> builder = new RunnableActionBundle.Builder<>(new Action<Integer>() {
			@Override
			public Integer action() throws Throwable {
				return value;
			}
		}).onCompleted(new Response.OnCompleted<Integer>() {
			@Override
			public void onCompleted(Integer response) {
				assertThat(response, is(value));
			}
		});
		new RunnableAction<>(builder.build(), getDefaultDeliverer()).run();
	}
	
	@Test
	public void run_onActionThrowAnException_onErrorCalled() {
		final NullPointerException npe = new NullPointerException();
		RunnableActionBundle.Builder<Void> builder = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				throw npe;
			}
		}).onError(new Response.OnError() {
			@Override
			public void onError(Throwable e) {
				Assert.assertEquals(e, npe);
			}
		});
		new RunnableAction<>(builder.build(), getDefaultDeliverer()).run();
	}
	
	@Test
	public void run_tryToRunOnCancelledState_listenerIsNotCalled() {
		RunnableActionBundle.Builder<Void> builder = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public Void action() throws Throwable {
				throw new IllegalStateException();
			}
		});
		RunnableAction<Void> action = new RunnableAction<>(builder.build(), getDefaultDeliverer());
		action.cancel();
		action.run();
	}
	
	@Test
	public void cancel_whileRunning_ResponsesIsNotCalled() throws InterruptedException {
		final CountDownLatch inputLatch = new CountDownLatch(1);
		RunnableActionBundle.Builder<Void> builder = new RunnableActionBundle.Builder<>(new Action<Void>() {
			@Override
			public synchronized Void action() throws Throwable {
				inputLatch.countDown();
				wait();
				return null;
			}
		})
			.onCompleted(new Response.OnCompleted<Void>() {
				@Override
				public void onCompleted(Void response) {
					fail();
				}
			})
			.onError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
					fail();
				}
			});
		final RunnableAction<Void> action = new RunnableAction<>(builder.build(), getDefaultDeliverer());
		Executors
			.newFixedThreadPool(1)
			.execute(new Runnable() {
				@Override
				public void run() {
					try {
						inputLatch.await();
						action.cancel();
					} catch (InterruptedException e) {
						fail();
					}
				}
			});
		action.run();
	}
	
}