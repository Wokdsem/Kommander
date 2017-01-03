package com.wokdsem.kommander;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

final class Executor {

	private final PoolExecutor poolExecutor;

	Executor(int poolSize) {
		this.poolExecutor = new PoolExecutor(poolSize);
	}

	void execute(Runnable runnable) {
		poolExecutor.execute(runnable);
	}

	private static class PoolExecutor extends ThreadPoolExecutor {

		private PoolExecutor(int poolSize) {
			super(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, buildBlockingQueue(), buildThreadFactory());
		}

		private static BlockingQueue<Runnable> buildBlockingQueue() {
			return new LinkedBlockingQueue<>();
		}

		private static ThreadFactory buildThreadFactory() {
			return new ThreadFactory() {
				private final AtomicInteger threadCount = new AtomicInteger(1);

				public Thread newThread(Runnable r) {
					return new Thread(r, "Kommander-ThreadExecutor #" + threadCount.getAndIncrement());
				}
			};
		}

	}

}
