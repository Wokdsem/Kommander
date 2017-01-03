package com.wokdsem.kommander;

import java.util.Locale;

public class Kommander {

	private static final int DEFAULT_CORE_POOL_SIZE;

	static {
		Runtime runtime = Runtime.getRuntime();
		int cpuCount = runtime.availableProcessors();
		DEFAULT_CORE_POOL_SIZE = Math.max(cpuCount, 2);
	}

	private final Deliverer deliverer;
	private final Executor executor;

	private Kommander(Deliverer deliverer, int poolSize) {
		this.deliverer = deliverer;
		this.executor = new Executor(poolSize);
	}

	public static Kommander getInstance(Deliverer deliverer) {
		return getInstance(deliverer, DEFAULT_CORE_POOL_SIZE);
	}

	public static Kommander getInstance(Deliverer deliverer, int poolSize) {
		assertDeliverer(deliverer);
		assertPoolSize(poolSize);
		return new Kommander(deliverer, poolSize);
	}

	private static void assertDeliverer(Deliverer deliverer) {
		if (deliverer == null) {
			throw new IllegalArgumentException("Illegal null deliverer to instantiate Kommander.");
		}
	}

	private static void assertPoolSize(int poolSize) {
		if (poolSize < 1) {
			String illegalPoolMessage = "Non positive (%d) pool size to instantiate Kommander";
			String errMessage = String.format(Locale.getDefault(), illegalPoolMessage, poolSize);
			throw new IllegalArgumentException(errMessage);
		}
	}

	public <T> Kommand<T> makeKommand(Action<T> action) {
		return new Kommand<>(action, deliverer, executor);
	}

}
