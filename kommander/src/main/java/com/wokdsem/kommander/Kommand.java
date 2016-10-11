package com.wokdsem.kommander;

public class Kommand<T> {

	private final ActionBundle.Builder<T> bundleBuilder;
	private final ExecutorDelegate delegate;

	Kommand(Action<T> action, Deliverer deliverer, long superId, ExecutorDelegate delegate) {
		this.bundleBuilder = new ActionBundle.Builder<>(action, deliverer, superId);
		this.delegate = delegate;
	}

	public Kommand<T> setOnCompleted(Response.OnCompleted<T> onCompleted) {
		bundleBuilder.onCompleted(onCompleted);
		return this;
	}

	public Kommand<T> setOnError(Response.OnError onError) {
		bundleBuilder.onError(onError);
		return this;
	}

	public Kommand<T> setTag(String tag) {
		bundleBuilder.tag(tag);
		return this;
	}

	public void kommand() {
		ActionBundle<T> actionBundle = bundleBuilder.build();
		delegate.execute(actionBundle);
	}

	interface ExecutorDelegate {

		void execute(ActionBundle actionBundle);

	}

}
