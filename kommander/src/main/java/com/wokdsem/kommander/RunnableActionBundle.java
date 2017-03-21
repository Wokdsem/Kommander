package com.wokdsem.kommander;

class RunnableActionBundle<T> {

	final Action<T> action;
	final Response.OnCompleted<T> onCompleted;
	final Response.OnError onError;

	private RunnableActionBundle(Builder<T> builder) {
		this.action = builder.action;
		this.onCompleted = builder.onCompleted;
		this.onError = builder.onError;
	}

	static class Builder<T> {

		private final Action<T> action;
		private Response.OnCompleted<T> onCompleted;
		private Response.OnError onError;

		Builder(Action<T> action) {
			this.action = action;
		}

		Builder<T> onCompleted(Response.OnCompleted<T> onCompleted) {
			this.onCompleted = onCompleted;
			return this;
		}

		Builder<T> onError(Response.OnError onError) {
			this.onError = onError;
			return this;
		}

		RunnableActionBundle<T> build() {
			return new RunnableActionBundle<>(this);
		}

	}

}
