package com.wokdsem.android.kommander;

import com.wokdsem.android.kommander.toolbox.SimpleKeyCollection;

import java.util.Collection;

class KommandEngine {

	private final KommandExecutor executor;
	private final KommandDeliverer deliverer;
	private final AfterKommandExecuted afterExecuted;
	private final SimpleKeyCollection<String, RunnableKommand> currentRunnableKommands;

	public KommandEngine(KommanderConfiguration configuration) {
		this.executor = getExecutor(configuration);
		this.deliverer = configuration.deliverer;
		this.afterExecuted = getAfterKommandExecuted();
		currentRunnableKommands = new SimpleKeyCollection<>();
	}

	private KommandExecutor getExecutor(KommanderConfiguration configuration) {
		int poolSize = configuration.poolSize;
		int maxPoolSize = configuration.maxPoolSize;
		long msKeepAlive = configuration.msKeepAlive;
		return new KommandExecutor(poolSize, maxPoolSize, msKeepAlive);
	}

	private AfterKommandExecuted getAfterKommandExecuted() {
		return new AfterKommandExecuted() {
			@Override
			public void onKommandExecuted(RunnableKommand kommand) {
				synchronized (currentRunnableKommands) {
					currentRunnableKommands.removeValue(kommand.tag, kommand);
				}
			}
		};
	}

	public <T> void executeKommand(KommandBundle<T> bundle) {
		RunnableKommand<T> rKommand = new RunnableKommand<>(bundle, deliverer, afterExecuted);
		synchronized (currentRunnableKommands) {
			currentRunnableKommands.put(rKommand.tag, rKommand);
		}
		executor.execute(rKommand);
	}

	public void cancelKommands(String tag) {
		if (tag == null) return;
		for (RunnableKommand runnableKommand : removeCollection(tag)) {
			runnableKommand.cancel();
		}
	}

	private Collection<RunnableKommand> removeCollection(String key) {
		synchronized (currentRunnableKommands) {
			return currentRunnableKommands.removeAll(key);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		executor.shutdown();
	}

}
