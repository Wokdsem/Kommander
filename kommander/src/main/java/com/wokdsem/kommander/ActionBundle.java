package com.wokdsem.kommander;

class ActionBundle<T> {

	final Action<T> action;
	final Deliverer deliverer;
	final RunnableActionTag runnableActionTag;
	final Response.OnCompleted<T> onCompleted;
	final Response.OnError onError;

	private ActionBundle(Builder<T> builder) {
		this.action = builder.action;
		this.deliverer = builder.deliverer;
		this.runnableActionTag = new RunnableActionTag(builder.superId, builder.tag);
		this.onCompleted = builder.onCompleted;
		this.onError = builder.onError;
	}

	static class Builder<T> {

		private final Action<T> action;
		private final Deliverer deliverer;
		private final long superId;
		private String tag;
		private Response.OnCompleted<T> onCompleted;
		private Response.OnError onError;

		public Builder(Action<T> action, Deliverer deliverer, long superId) {
			this.action = action;
			this.deliverer = deliverer;
			this.superId = superId;
		}

		public Builder tag(String tag) {
			this.tag = tag;
			return this;
		}

		public Builder onCompleted(Response.OnCompleted<T> onCompleted) {
			this.onCompleted = onCompleted;
			return this;
		}

		public Builder onError(Response.OnError onError) {
			this.onError = onError;
			return this;
		}

		public ActionBundle<T> build() {
			return new ActionBundle<>(this);
		}

	}

}
