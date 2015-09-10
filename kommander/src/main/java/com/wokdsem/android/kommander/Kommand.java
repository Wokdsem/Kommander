package com.wokdsem.android.kommander;

public class Kommand<T> {

	private final KommandBundle<T> bundle;
	private final KommandEngine engine;

	Kommand(Action<T> action, KommandEngine engine){
		this.bundle = new KommandBundle<>(action);
		this.engine = engine;
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
		engine.executeKommand(bundle);
	}

}
