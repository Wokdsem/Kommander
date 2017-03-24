package com.wokdsem.kommander;

class RunnableAction<T> implements Runnable, Cancelable {
	
	private Action<T> action;
	private Deliverer deliverer;
	private Response.OnCompleted<T> onCompleted;
	private Response.OnError onError;
	private RunnableState state;
	private Thread executor;
	private Throwable error;
	private T result;
	
	RunnableAction(RunnableActionBundle<T> bundle, Deliverer deliverer) {
		this.action = bundle.action;
		this.deliverer = deliverer;
		this.onCompleted = bundle.onCompleted;
		this.onError = bundle.onError;
		this.state = RunnableState.NEW;
	}
	
	@Override
	public final void run() {
		Action<T> runnableAction;
		synchronized (this) {
			if (state != RunnableState.NEW) {
				return;
			}
			runnableAction = action;
			executor = Thread.currentThread();
			state = RunnableState.RUNNING;
		}
		try {
			T result = runnableAction.action();
			onResult(result);
		} catch (Throwable e) {
			onError(e);
		} finally {
			synchronized (this) {
				action = null;
				deliverer = null;
				executor = null;
			}
		}
	}
	
	private synchronized void onResult(T result) {
		if (state != RunnableState.CANCELED) {
			if (onCompleted != null) {
				this.result = result;
				onError = null;
				deliverer.deliver(new Runnable() {
					@Override
					public void run() {
						deliverResult();
					}
				});
			} else {
				release(true);
			}
		}
	}
	
	private synchronized void deliverResult() {
		if (state != RunnableState.CANCELED) {
			onCompleted.onCompleted(result);
			release(true);
		}
	}
	
	private synchronized void onError(Throwable error) {
		if (state != RunnableState.CANCELED) {
			if (onError != null) {
				this.error = error;
				onCompleted = null;
				deliverer.deliver(new Runnable() {
					@Override
					public void run() {
						deliverError();
					}
				});
			} else {
				release(true);
			}
		}
	}
	
	private synchronized void deliverError() {
		if (state != RunnableState.CANCELED) {
			onError.onError(error);
			release(true);
		}
	}
	
	@Override
	public synchronized void cancel() {
		if (state == RunnableState.NEW || state == RunnableState.RUNNING) {
			if (executor != null) {
				executor.interrupt();
			}
			action = null;
			deliverer = null;
			release(false);
		}
	}
	
	private void release(boolean isFullyCompleted) {
		this.state = isFullyCompleted ? RunnableState.COMPLETED : RunnableState.CANCELED;
		onCompleted = null;
		onError = null;
		result = null;
		error = null;
	}
	
	private enum RunnableState {
		NEW,
		RUNNING,
		COMPLETED,
		CANCELED
	}
	
}
