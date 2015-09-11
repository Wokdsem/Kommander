package com.wokdsem.android.kommander;

public class Kommander {

	private final KommandEngine engine;

	public static Kommander getInstance(KommandDeliverer kommandDeliverer) {
		KommanderConfiguration.Builder configurationBuilder = KommanderConfiguration.build(kommandDeliverer);
		return Kommander.getInstance(configurationBuilder.getExecutorConfig());
	}

	public static <E extends Exception> Kommander getInstance(KommanderConfiguration configuration) {
		return new Kommander(configuration);
	}

	private Kommander(KommanderConfiguration configuration) {
		this.engine = new KommandEngine(configuration);
	}

	public <T> Kommand<T> makeKommand(Action<T> action) {
		return new Kommand<>(action, engine);
	}

	public void cancelKommands(String tag) {
		engine.cancelKommands(tag);
	}

}
