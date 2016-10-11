package com.wokdsem.kommander;

public class CentralKommander {

	static final Deliverer DEFAULT_DELIVERER;

	static {
		DEFAULT_DELIVERER = new Deliverer() {
			@Override
			public void deliver(Runnable runnable) {
				runnable.run();
			}
		};
	}

	private final ActionsEngine engine;
	private Koncurrent koncurrent;
	private long prevSuperId;

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
		this.engine = new ActionsEngine(config);
	}

	public Kommander buildKommander() {
		return buildKommander(DEFAULT_DELIVERER);
	}

	public Kommander buildKommander(Deliverer deliverer) {
		if (deliverer == null) {
			throw new IllegalArgumentException("Illegal null deliverer to build Kommander.");
		}
		return new Kommander(getSuperId(), engine, deliverer);
	}

	public Koncurrent getKoncurrent() {
		if (koncurrent == null) {
			synchronized (this) {
				if (koncurrent == null) {
					koncurrent = new Koncurrent(engine, getSuperId());
				}
			}
		}
		return koncurrent;
	}

	private synchronized long getSuperId() {
		long superId = prevSuperId;
		while (superId == prevSuperId) {
			superId = System.currentTimeMillis();
		}
		prevSuperId = superId;
		return superId;
	}

}
