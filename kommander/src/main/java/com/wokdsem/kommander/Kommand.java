package com.wokdsem.kommander;

import java.lang.ref.WeakReference;

/**
 * A {@code Kommand} is an asynchronous context builder of an {@link Action} and the environment where the Action will
 * be executed. {@code Kommand} makes easy setting up the response (successful or erroneous) handlers to the future
 * calculation from the {@link Action} execution. When a Kommand is required to launch an asynchronous execution, takes
 * the current context and prepares a request to launch to its asynchronous executor.
 * When a {@code Kommand} is launched can be canceled with a {@link KommandToken}, where: the {@code Kommand} is not
 * executed if the computation has not started yet, trying to stop the computation when is running, or at least, the
 * computation is not delivered when that is finished. You can lean on {@link KommandTokenBox} to get an advanced
 * managing of the cancellation of a set of launched {@code Kommand}.
 *
 * @param <T> the type of the {@link Kommand}
 */
public class Kommand<T> {
	
	private final RunnableActionBundle.Builder<T> bundleBuilder;
	private final Executor executor;
	
	Kommand(Action<T> action, Deliverer deliverer, Executor executor) {
		this.bundleBuilder = new RunnableActionBundle.Builder<>(action, deliverer);
		this.executor = executor;
	}
	
	/**
	 * Sets to the {@code Kommand} the callback to an successful calculation.
	 *
	 * @param onCompleted value that is called with {@code T} calculation, a {@code null} value is allowed.
	 */
	public Kommand<T> setOnCompleted(Response.OnCompleted<T> onCompleted) {
		bundleBuilder.onCompleted(onCompleted);
		return this;
	}
	
	/**
	 * Sets to the {@code Kommand} the callback to an erroneous calculation.
	 *
	 * @param onError value that is called with the {@link Throwable} that caused the fail.
	 */
	public Kommand<T> setOnError(Response.OnError onError) {
		bundleBuilder.onError(onError);
		return this;
	}
	
	/**
	 * Requests to execute the current kommand context in the future.
	 *
	 * @return a KommandToken that enables to cancel the asynchronous execution.
	 */
	public KommandToken kommand() {
		RunnableActionBundle<T> bundle = bundleBuilder.build();
		RunnableAction<T> runnableAction = new RunnableAction<>(bundle);
		executor.execute(runnableAction);
		return new WeakKommandToken(runnableAction);
	}

	/**
	 * Requests to execute the current kommand context in the future.
	 *
	 * @param tokenBox where {@link KommandToken} is delegated
	 */
	public void kommand(KommandTokenBox tokenBox) {
		assertTokenBox(tokenBox);
		tokenBox.append(kommand());
	}

	/**
	 * Requests to execute the current kommand context in the future.
	 *
	 * @param tokenBox where {@link KommandToken} is delegated
	 * @param tag value used when {@link KommandToken} is delegated on {@link KommandTokenBox}
	 */
	public <K> void kommand(KommandTokenBox tokenBox, K tag) {
		assertTokenBox(tokenBox);
		tokenBox.append(kommand(), tag);
	}

	private void assertTokenBox(KommandTokenBox tokenBox) {
		if (tokenBox == null) {
			throw new IllegalArgumentException("Null tokenBox is not allowed");
		}
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
