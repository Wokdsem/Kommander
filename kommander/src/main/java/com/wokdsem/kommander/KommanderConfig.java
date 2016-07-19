package com.wokdsem.kommander;

public class KommanderConfig {

	private static final int DEFAULT_CORE_POOL_SIZE;

	static {
		int cpuCount = Runtime.getRuntime().availableProcessors();
		DEFAULT_CORE_POOL_SIZE = Math.max(cpuCount, 2);
	}

	final int poolSize;

	private KommanderConfig(Builder builder) {
		this.poolSize = builder.poolSize;
	}

	public static class Builder {

		private int poolSize = DEFAULT_CORE_POOL_SIZE;

		public Builder poolSize(int poolSize) {
			this.poolSize = poolSize;
			return this;
		}

		public KommanderConfig getConfig() {
			return new KommanderConfig(this);
		}

	}

}
