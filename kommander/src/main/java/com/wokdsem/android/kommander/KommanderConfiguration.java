package com.wokdsem.android.kommander;

public class KommanderConfiguration {

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int DEFAULT_CORE_POOL_SIZE = Math.max(CPU_COUNT, 2);
	private static final int DEFAULT_MAXIMUM_CORE_POOL_SIZE = DEFAULT_CORE_POOL_SIZE * 2;
	private static final long DEFAULT_MS_KEEP_ALIVE = 100_000;

	final KommandDeliverer deliverer;
	final int poolSize;
	final int maxPoolSize;
	final long msKeepAlive;

	private KommanderConfiguration(Builder builder) {
		this.deliverer = builder.deliverer;
		this.poolSize = builder.poolSize;
		this.maxPoolSize = builder.maxPoolSize;
		this.msKeepAlive = builder.msKeepAlive;
	}

	public static Builder build(KommandDeliverer deliverer) {
		return new Builder(deliverer);
	}

	public static class Builder {

		private KommandDeliverer deliverer;
		private int poolSize;
		private int maxPoolSize;
		private long msKeepAlive;

		private Builder(KommandDeliverer deliverer) {
			initializeBuilder(deliverer);
		}

		private void initializeBuilder(KommandDeliverer deliverer) {
			if (deliverer == null)
				throw new IllegalArgumentException("Null KommandDeliverer received.");
			this.deliverer = deliverer;
			this.poolSize = DEFAULT_CORE_POOL_SIZE;
			this.maxPoolSize = DEFAULT_MAXIMUM_CORE_POOL_SIZE;
			this.msKeepAlive = DEFAULT_MS_KEEP_ALIVE;
		}

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

		public KommanderConfiguration getExecutorConfig() {
			return new KommanderConfiguration(this);
		}

	}

}
