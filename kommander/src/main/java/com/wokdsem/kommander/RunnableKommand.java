package com.wokdsem.kommander;

import static com.wokdsem.kommander.RunnableState.*;

class RunnableKommand<T> implements Runnable {

	public final KommandTag tag;

	private final Action<T> action;
	private final KommandDeliverer deliverer;
	private final AfterKommandExecuted afterExecuted;

	private Thread executor;
	private RunnableState state;
	private Response.OnError onError;
	private Response.OnCompleted<T> onCompleted;

	public RunnableKommand(KommandBundle<T> bundle, AfterKommandExecuted afterExecuted) {
		this.tag = bundle.getKommandTag();
		this.action = bundle.action;
		this.deliverer = bundle.deliverer;
		this.afterExecuted = afterExecuted;
		this.onError = bundle.onError;
		this.onCompleted = bundle.onCompleted;
		this.state = NEW;
	}

	@Override
	public final void run() {
		synchronized (this) {
			if (state == CANCELED) return;
			executor = Thread.currentThread();
			state = RUNNING;
		}
		try {
			T result = action.kommandAction();
			deliverResponse(result);
		} catch (Throwable e) {
			deliverError(e);
		} finally {
			synchronized (this) {
				executor = null;
			}
		}
	}

	private void deliverResponse(final T response) {
		deliverer.deliver(new Runnable() {
			@Override
			public void run() {
				synchronized (RunnableKommand.this) {
					if (state == CANCELED) return;
					if (onCompleted != null) onCompleted.onCompleted(response);
					afterExecuted();
				}
			}
		});
	}

	private void deliverError(final Throwable e) {
		deliverer.deliver(new Runnable() {
			@Override
			public void run() {
				synchronized (RunnableKommand.this) {
					if (state == CANCELED) return;
					if (onError != null) onError.onError(e);
					afterExecuted();
				}
			}
		});
	}

	private void afterExecuted() {
		state = COMPLETED;
		afterExecuted.onKommandExecuted(this);
	}

	synchronized void cancel() {
		if (state == NEW || state == RUNNING) {
			if (executor != null) executor.interrupt();
			onCompleted = null;
			onError = null;
			state = CANCELED;
		}
	}

}
