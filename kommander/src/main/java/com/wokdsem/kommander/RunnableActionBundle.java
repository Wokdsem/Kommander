package com.wokdsem.kommander;

import static com.wokdsem.kommander.Deliverers.DEFAULT_DELIVERER;

class RunnableActionBundle<T> {
	
	final Action<T> action;
	final Response.OnCompleted<T> onCompleted;
	final Response.OnError onError;
	final Deliverer deliverer;
	
	private RunnableActionBundle(Builder<T> builder, Deliverer deliverer) {
		this.action = builder.action;
		this.onCompleted = builder.onCompleted;
		this.onError = builder.onError;
		this.deliverer = deliverer;
	}
	
	static class Builder<T> {
		
		private final Action<T> action;
		private Response.OnCompleted<T> onCompleted;
		private Response.OnError onError;
		private Deliverer deliverer;
		
		Builder(Action<T> action) {
			this.action = action;
		}
		
		Builder<T> deliverer(Deliverer deliverer) {
			this.deliverer = deliverer;
			return this;
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
			Deliverer actionDeliverer = deliverer == null ? DEFAULT_DELIVERER : deliverer;
			return new RunnableActionBundle<>(this, actionDeliverer);
		}
		
	}
	
}
