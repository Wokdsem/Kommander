package com.wokdsem.kommander;

public class Kommander {

	private final long kommanderId;
	private final KommandEngine engine;
	private final KommandDeliverer deliverer;
	private final Kommand.ExecutorDelegate executorDelegate;

	Kommander(long kommanderId, final KommandEngine engine, KommandDeliverer deliverer) {
		this.kommanderId = kommanderId;
		this.engine = engine;
		this.deliverer = deliverer;
		this.executorDelegate = new Kommand.ExecutorDelegate() {
			@Override
			public void execute(KommandBundle bundle) {
				engine.executeKommand(bundle);
			}
		};
	}

	public <T> Kommand<T> makeKommand(Action<T> action) {
		KommandBundle<T> bundle = new KommandBundle<>(action, deliverer, kommanderId);
		return new Kommand<>(bundle, executorDelegate);
	}

	public void cancelKommands(String tag) {
		engine.cancelKommands(getKommandTag(tag));
	}

	private KommandTag getKommandTag(String tag) {
		return tag == null ? null : new KommandTag(kommanderId, tag);
	}

}
