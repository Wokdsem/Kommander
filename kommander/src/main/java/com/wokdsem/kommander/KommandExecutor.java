package com.wokdsem.kommander;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

final class KommandExecutor extends ThreadPoolExecutor {

	private static BlockingQueue<Runnable> buildBlockingQueue() {
		return new LinkedBlockingQueue<>();
	}

	private static ThreadFactory buildThreadFactory() {
		return new ThreadFactory() {
			private final AtomicInteger threadCount = new AtomicInteger(1);

			@SuppressWarnings("NullableProblems")
			public Thread newThread(Runnable r) {
				return new Thread(r, "KommandExecutor #" + threadCount.getAndIncrement());
			}
		};
	}

	public KommandExecutor(int corePoolSize, int maxCorePoolSize, long msKeepAlive) {
		super(corePoolSize, maxCorePoolSize, msKeepAlive, TimeUnit.MILLISECONDS, buildBlockingQueue(), buildThreadFactory());
	}

}