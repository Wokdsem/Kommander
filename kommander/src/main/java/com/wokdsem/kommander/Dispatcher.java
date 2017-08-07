package com.wokdsem.kommander;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Dispatcher {
	
	private final Executor executor;
	private ScheduledExecutorService scheduledExecutor;
	
	Dispatcher(Executor executor) {
		this.executor = executor;
	}
	
	<T> KommandToken kommand(RunnableActionBundle<T> bundle, long delay) {
		return delay > 0 ? delayKommand(bundle, delay) : rawKommand(bundle);
	}
	
	private <T> KommandToken delayKommand(RunnableActionBundle<T> bundle, long delay) {
		ScheduledRunnableAction<T> runnableAction = new ScheduledRunnableAction<>(this, bundle);
		getScheduledExecutor().schedule(runnableAction, delay, TimeUnit.MILLISECONDS);
		return KommandTokens.newSimpleKommandToken(runnableAction);
	}
	
	private synchronized ScheduledExecutorService getScheduledExecutor() {
		if (scheduledExecutor == null) {
			scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		}
		return scheduledExecutor;
	}
	
	private <T> KommandToken rawKommand(RunnableActionBundle<T> bundle) {
		RunnableAction<T> runnableAction = new RunnableAction<>(bundle);
		executor.execute(runnableAction);
		return KommandTokens.newWeakKommandToken(runnableAction);
	}
	
}
