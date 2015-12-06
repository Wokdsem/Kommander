package com.wokdsem.kommander;

public class KommanderConfig {

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int DEFAULT_CORE_POOL_SIZE = Math.max(CPU_COUNT, 2);
	private static final int DEFAULT_MAXIMUM_CORE_POOL_SIZE = DEFAULT_CORE_POOL_SIZE * 2;
	private static final long DEFAULT_MS_KEEP_ALIVE = 100_000;

	final int poolSize;
	final int maxPoolSize;
	final long msKeepAlive;

	private KommanderConfig(Builder builder) {
		this.poolSize = builder.poolSize;
		this.maxPoolSize = builder.maxPoolSize;
		this.msKeepAlive = builder.msKeepAlive;
	}

	public static class Builder {

		private int poolSize = DEFAULT_CORE_POOL_SIZE;
		private int maxPoolSize = DEFAULT_MAXIMUM_CORE_POOL_SIZE;
		private long msKeepAlive = DEFAULT_MS_KEEP_ALIVE;

		public Builder setPoolSize(int poolSize) {
			this.poolSize = poolSize;
			return this;
		}

		public Builder setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
			return this;
		}

		public Builder setMsKeepAlive(long msKeepAlive) {
			this.msKeepAlive = msKeepAlive;
			return this;
		}

		public KommanderConfig getConfig() {
			return new KommanderConfig(this);
		}

	}

}
