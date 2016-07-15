package com.wokdsem.kommander;

class RunnableKommand<T> implements Runnable {

	private enum RunnableState {
		NEW,
		RUNNING,
		COMPLETED,
		CANCELED
	}

	public final KommandTag tag;
	private final KommandDeliverer deliverer;
	private final AfterKommandExecuted afterExecuted;

	private Thread executor;
	private RunnableState state;
	private Action<T> action;
	private Response.OnError onError;
	private Response.OnCompleted<T> onCompleted;

	public RunnableKommand(KommandBundle<T> bundle, AfterKommandExecuted afterExecuted) {
		this.tag = bundle.getKommandTag();
		this.deliverer = bundle.deliverer;
		this.afterExecuted = afterExecuted;
		this.action = bundle.action;
		this.onError = bundle.onError;
		this.onCompleted = bundle.onCompleted;
		this.state = RunnableState.NEW;
	}

	@Override
	public final void run() {
		Action<T> runnableAction;
		synchronized (this) {
			if (state == RunnableState.CANCELED) {
				return;
			}
			runnableAction = action;
			executor = Thread.currentThread();
			state = RunnableState.RUNNING;
		}
		try {
			T result = runnableAction.kommandAction();
			deliverResponse(result);
		} catch (Throwable e) {
			deliverError(e);
		} finally {
			synchronized (this) {
				action = null;
				executor = null;
			}
		}
	}

	private void deliverResponse(final T response) {
		deliverer.deliver(new Runnable() {
			@Override
			public void run() {
				synchronized (RunnableKommand.this) {
					if (state != RunnableState.CANCELED) {
						if (onCompleted != null) {
							onCompleted.onCompleted(response);
						}
						afterExecuted();
					}
				}
			}
		});
	}

	private void deliverError(final Throwable e) {
		deliverer.deliver(new Runnable() {
			@Override
			public void run() {
				synchronized (RunnableKommand.this) {
					if (state != RunnableState.CANCELED) {
						if (onError != null) {
							onError.onError(e);
						}
						afterExecuted();
					}
				}
			}
		});
	}

	private void afterExecuted() {
		state = RunnableState.COMPLETED;
		afterExecuted.onKommandExecuted(this);
	}

	synchronized void cancel() {
		if (state == RunnableState.NEW || state == RunnableState.RUNNING) {
			if (executor != null) {
				executor.interrupt();
			}
			action = null;
			onCompleted = null;
			onError = null;
			state = RunnableState.CANCELED;
		}
	}

}
