package com.wokdsem.kommander;

import java.util.concurrent.atomic.AtomicLong;

public class CentralKommander {

	private static final KommandDeliverer DEFAULT_DELIVERER;

	static {
		DEFAULT_DELIVERER = new KommandDeliverer() {
			@Override
			public void deliver(Runnable runnable) {
				runnable.run();
			}
		};
	}

	private final KommandEngine engine;
	private final AtomicLong idGenerator;

	public static CentralKommander getInstance() {
		KommanderConfig.Builder defaultConfig = new KommanderConfig.Builder();
		return getInstance(defaultConfig.getConfig());
	}

	public static <E extends Exception> CentralKommander getInstance(KommanderConfig config) {
		if (config == null) {
			throw new IllegalArgumentException("Illegal null KommanderConfig to instantiate CentralKommander.");
		}
		return new CentralKommander(config);
	}

	private CentralKommander(KommanderConfig config) {
		this.engine = new KommandEngine(config);
		this.idGenerator = new AtomicLong(0);
	}

	public Kommander buildKommander() {
		return buildKommander(DEFAULT_DELIVERER);
	}

	public Kommander buildKommander(KommandDeliverer deliverer) {
		if (deliverer == null) {
			throw new IllegalArgumentException("Illegal null deliverer to build Kommander.");
		}
		return new Kommander(idGenerator.incrementAndGet(), engine, deliverer);
	}

}
