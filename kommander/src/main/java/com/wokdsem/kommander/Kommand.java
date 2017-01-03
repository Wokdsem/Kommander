package com.wokdsem.kommander;

import java.lang.ref.WeakReference;

public class Kommand<T> {

	private final RunnableActionBundle.Builder<T> bundleBuilder;
	private final Executor executor;

	Kommand(Action<T> action, Deliverer deliverer, Executor executor) {
		this.bundleBuilder = new RunnableActionBundle.Builder<>(action, deliverer);
		this.executor = executor;
	}

	public Kommand<T> setOnCompleted(Response.OnCompleted<T> onCompleted) {
		bundleBuilder.onCompleted(onCompleted);
		return this;
	}

	public Kommand<T> setOnError(Response.OnError onError) {
		bundleBuilder.onError(onError);
		return this;
	}

	public KommandToken kommand() {
		RunnableActionBundle<T> bundle = bundleBuilder.build();
		RunnableAction<T> runnableAction = new RunnableAction<>(bundle);
		executor.execute(runnableAction);
		return new WeakKommandToken(runnableAction);
	}

	private static class WeakKommandToken implements KommandToken {

		private final WeakReference<RunnableAction> actionReference;

		private WeakKommandToken(RunnableAction runnableAction) {
			this.actionReference = new WeakReference<>(runnableAction);
		}

		@Override
		public void cancel() {
			RunnableAction runnableAction = actionReference.get();
			if (runnableAction != null) {
				runnableAction.cancel();
			}
		}
	}

}
