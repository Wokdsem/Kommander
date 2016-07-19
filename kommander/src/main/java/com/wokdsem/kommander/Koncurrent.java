package com.wokdsem.kommander;

import com.wokdsem.kommander.toolbox.CountDownLock;

import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Koncurrent {

	private static final AtomicLong TAG_GEN;
	private static final Deliverer DELIVERER;

	static {
		TAG_GEN = new AtomicLong();
		DELIVERER = new Deliverer() {
			@Override
			public void deliver(Runnable runnable) {
				runnable.run();
			}
		};
	}

	private final ActionsEngine engine;
	private final long koncurrentId;

	Koncurrent(ActionsEngine engine, long koncurrentId) {
		this.engine = engine;
		this.koncurrentId = koncurrentId;
	}

	@SuppressWarnings({"unchecked"})
	public MultipleResults kommand(Action<?>... actions) throws KoncurrentException {
		int actionsLength = actions.length;
		Object[] responses = new Object[actionsLength];
		CountDownLock lock = new CountDownLock(actionsLength);
		String koncurrentTag = Long.toString(TAG_GEN.getAndIncrement());
		RunnableActionTag actionTag = new RunnableActionTag(koncurrentId, koncurrentTag);
		KoncurrentError koncurrentError = new KoncurrentError(lock, actionTag);
		Response.OnError onErrorResponse = new OnErrorResponse(koncurrentError);
		LinkedList<Runnable> runnableActions = new LinkedList<>();

		for (int i = 0; i < actionsLength; i++) {
			Action<Object> action = (Action<Object>) actions[i];
			ActionBundle actionBundle = new ActionBundle.Builder<>(action, DELIVERER, koncurrentId)
					.onCompleted(new OnCompletedResponse(lock, responses, i))
					.onError(onErrorResponse)
					.tag(koncurrentTag)
					.build();
			Runnable runnableAction = engine.executeAction(actionBundle);
			runnableActions.add(runnableAction);
		}
		runActions(runnableActions);
		waitResponses(lock, koncurrentError);

		return new MultipleResults(responses);
	}

	private void runActions(LinkedList<Runnable> runnableActions) {
		Thread currentThread = Thread.currentThread();
		while (!runnableActions.isEmpty()) {
			if (currentThread.isInterrupted()) {
				runnableActions.clear();
			} else {
				Runnable runnableAction = runnableActions.pollLast();
				runnableAction.run();
			}
		}
	}

	private void waitResponses(CountDownLock lock, KoncurrentError koncurrentError) throws KoncurrentException {
		try {
			if (!lock.await()) {
				koncurrentError.onError();
				throw new KoncurrentException();
			}
		} catch (InterruptedException e) {
			koncurrentError.onError();
			throw new KoncurrentException(e);
		}
	}

	private class KoncurrentError {

		private final CountDownLock lock;
		private final RunnableActionTag tag;
		private final AtomicBoolean consumed;

		KoncurrentError(CountDownLock lock, RunnableActionTag tag) {
			this.lock = lock;
			this.tag = tag;
			this.consumed = new AtomicBoolean(false);
		}

		public void onError() {
			if (consumed.compareAndSet(false, true)) {
				engine.cancelKommands(tag);
				lock.releaseLock();
			}
		}

	}

	private class OnCompletedResponse implements Response.OnCompleted {

		private final CountDownLock lock;
		private final Object[] responses;
		private final int index;

		private OnCompletedResponse(CountDownLock lock, Object[] responses, int index) {
			this.lock = lock;
			this.responses = responses;
			this.index = index;
		}

		@Override
		public void onCompleted(Object response) {
			responses[index] = response;
			lock.countDown();
		}

	}

	private class OnErrorResponse implements Response.OnError {

		private final KoncurrentError koncurrentError;

		private OnErrorResponse(KoncurrentError koncurrentError) {
			this.koncurrentError = koncurrentError;
		}

		@Override
		public void onError(Throwable e) {
			koncurrentError.onError();
		}

	}

	public static class MultipleResults {

		private final Object[] response;

		private MultipleResults(Object[] response) {
			this.response = response;
		}

		@SuppressWarnings("unchecked")
		public <T> T get(int index) {
			if (index < 0 || index >= response.length) {
				String err = String.format(Locale.getDefault(), "Index: %d, Size: %d", index, response.length);
				throw new IndexOutOfBoundsException();
			}
			return (T) response[index];
		}

	}

}
