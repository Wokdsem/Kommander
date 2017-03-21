package com.wokdsem.kommander;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.max;
import static java.lang.Runtime.getRuntime;

final class KommandExecutor implements Executor {
	
	private final PoolExecutor executor;
	
	static KommandExecutor newInstance() {
		int minPoolSize = max(getRuntime().availableProcessors() / 2, 2);
		return new KommandExecutor(minPoolSize);
	}
	
	private KommandExecutor(int maxPoolSize) {
		this.executor = new PoolExecutor(maxPoolSize);
	}
	
	@Override
	public void execute(Runnable runnable) {
		executor.execute(runnable);
	}
	
	private static class PoolExecutor extends ThreadPoolExecutor {
		
		private PoolExecutor(int minPoolSize) {
			super(minPoolSize, Integer.MAX_VALUE, 50_000L, TimeUnit.MILLISECONDS, buildBlockingQueue(), buildThreadFactory());
		}
		
		private static BlockingQueue<Runnable> buildBlockingQueue() {
			return new SynchronousQueue<>();
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
