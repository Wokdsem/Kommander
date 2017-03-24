package com.wokdsem.kommander;

class ScheduledRunnableAction<T> implements Runnable, Cancelable {
	
	private Dispatcher dispatcher;
	private RunnableActionBundle<T> bundle;
	private KommandToken wrappedToken;
	private ScheduledState state;
	
	ScheduledRunnableAction(Dispatcher dispatcher, RunnableActionBundle<T> bundle) {
		this.dispatcher = dispatcher;
		this.bundle = bundle;
		this.state = ScheduledState.PENDING;
	}
	
	@Override
	public synchronized void run() {
		if (state == ScheduledState.PENDING) {
			wrappedToken = dispatcher.kommand(bundle, 0L);
			state = ScheduledState.DELIVERED;
			release();
		}
	}
	
	@Override
	public synchronized void cancel() {
		if (state == ScheduledState.PENDING) {
			state = ScheduledState.CANCELED;
			release();
		} else if (state == ScheduledState.DELIVERED) {
			wrappedToken.cancel();
		}
	}
	
	private void release() {
		dispatcher = null;
		bundle = null;
	}
	
	private enum ScheduledState {
		PENDING,
		DELIVERED,
		CANCELED
	}
	
}
