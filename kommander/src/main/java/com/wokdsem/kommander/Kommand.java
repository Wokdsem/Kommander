package com.wokdsem.kommander;

public class Kommand<T> {

	private final KommandBundle<T> bundle;
	private final ExecutorDelegate delegate;

	Kommand(KommandBundle<T> bundle, ExecutorDelegate delegate) {
		this.bundle = bundle;
		this.delegate = delegate;
	}

	public Kommand<T> setOnCompleted(Response.OnCompleted<T> onCompleted) {
		bundle.onCompleted = onCompleted;
		return this;
	}

	public Kommand<T> setOnError(Response.OnError onError) {
		bundle.onError = onError;
		return this;
	}

	public Kommand<T> setTag(String tag) {
		bundle.tag = tag;
		return this;
	}

	public void kommand() {
		delegate.execute(bundle);
	}

	interface ExecutorDelegate {

		void execute(KommandBundle bundle);

	}

}
