package com.wokdsem.kommander;

public class Kommander {

	private final long kommanderId;
	private final ActionsEngine engine;
	private final Deliverer deliverer;
	private final Kommand.ExecutorDelegate executorDelegate;

	Kommander(long kommanderId, final ActionsEngine engine, Deliverer deliverer) {
		this.kommanderId = kommanderId;
		this.engine = engine;
		this.deliverer = deliverer;
		this.executorDelegate = new Kommand.ExecutorDelegate() {
			@Override
			public void execute(ActionBundle actionBundle) {
				engine.executeAction(actionBundle);
			}
		};
	}

	public <T> Kommand<T> makeKommand(Action<T> action) {
		return new Kommand<>(action, deliverer, kommanderId, executorDelegate);
	}

	public void cancelKommands(String tag) {
		RunnableActionTag runnableActionTag = getKommandTag(tag);
		engine.cancelKommands(runnableActionTag);
	}

	private RunnableActionTag getKommandTag(String tag) {
		return tag == null ? null : new RunnableActionTag(kommanderId, tag);
	}

}
