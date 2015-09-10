package com.wokdsem.android.kommander;

class RunnableKommand<T> implements Runnable {

	private final KommandBundle<T> bundle;
	private final KommandDeliverer deliverer;
	private final AfterKommandExecuted afterExecuted;
	private volatile boolean isCanceled;

	public RunnableKommand(KommandBundle<T> bundle, KommandDeliverer deliverer, AfterKommandExecuted afterExecuted) {
		this.bundle = bundle;
		this.deliverer = deliverer;
		this.afterExecuted = afterExecuted;
	}

	@Override
	public final void run() {
		if (isCanceled) return;
		try {
			Action<T> action = bundle.action;
			T result = action.kommandAction();
			deliverResponse(result);
		} catch (Throwable e) {
			deliverError(e);
		}
	}

	String getTag() {
		return bundle.tag;
	}

	private void deliverResponse(final T response) {
		deliverer.deliver(new Runnable() {
			@Override
			public void run() {
				synchronized (RunnableKommand.this) {
					if (isCanceled) return;
					Response.OnCompleted<T> onCompleted = bundle.onCompleted;
					if (onCompleted != null) onCompleted.onCompleted(response);
					notifyAfterExecuted();
				}
			}
		});
	}

	private void deliverError(final Throwable e) {
		deliverer.deliver(new Runnable() {
			@Override
			public void run() {
				synchronized (RunnableKommand.this) {
					if (isCanceled) return;
					Response.OnError onError = bundle.onError;
					if (onError != null) onError.onError(e);
					notifyAfterExecuted();
				}
			}
		});
	}

	private void notifyAfterExecuted() {
		afterExecuted.onKommandExecuted(this);
	}

	synchronized void cancel() {
		isCanceled = true;
	}

}
