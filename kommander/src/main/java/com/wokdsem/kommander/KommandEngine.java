package com.wokdsem.kommander;

import com.wokdsem.kommander.toolbox.SimpleKeyCollection;

import java.util.Collection;

class KommandEngine {

	private final KommandExecutor executor;
	private final AfterKommandExecuted afterExecuted;
	private final SimpleKeyCollection<KommandTag, RunnableKommand> currentRunnableKommands;

	public KommandEngine(KommanderConfig configuration) {
		this.executor = getExecutor(configuration);
		this.afterExecuted = getAfterKommandExecuted();
		currentRunnableKommands = new SimpleKeyCollection<>();
	}

	private KommandExecutor getExecutor(KommanderConfig configuration) {
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
		RunnableKommand<T> rKommand = new RunnableKommand<>(bundle, afterExecuted);
		synchronized (currentRunnableKommands) {
			currentRunnableKommands.put(rKommand.tag, rKommand);
		}
		executor.execute(rKommand);
	}

	public void cancelKommands(KommandTag tag) {
		if (tag != null) {
			for (RunnableKommand runnableKommand : removeCollection(tag)) {
				runnableKommand.cancel();
			}
		}
	}

	private Collection<RunnableKommand> removeCollection(KommandTag key) {
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
