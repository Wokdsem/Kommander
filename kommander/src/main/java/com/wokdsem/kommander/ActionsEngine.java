package com.wokdsem.kommander;

import com.wokdsem.kommander.toolbox.SimpleKeyCollection;

import java.util.Collection;

final class ActionsEngine {

	private final RunnableActionExecutor executor;
	private final AfterActionExecuted afterExecuted;
	private final SimpleKeyCollection<RunnableActionTag, RunnableAction> currentRunnableActions;

	ActionsEngine(KommanderConfig configuration) {
		this.executor = new RunnableActionExecutor(configuration.poolSize);
		this.afterExecuted = getAfterActionExecuted();
		currentRunnableActions = new SimpleKeyCollection<>();
	}

	private AfterActionExecuted getAfterActionExecuted() {
		return new AfterActionExecuted() {
			@Override
			public void onActionExecuted(RunnableAction runnableAction) {
				synchronized (currentRunnableActions) {
					currentRunnableActions.removeValue(runnableAction.tag, runnableAction);
				}
			}
		};
	}

	<T> Runnable executeAction(ActionBundle<T> bundle) {
		RunnableAction<T> rKommand = new RunnableAction<>(bundle, afterExecuted);
		synchronized (currentRunnableActions) {
			currentRunnableActions.put(rKommand.tag, rKommand);
		}
		executor.execute(rKommand);
		return rKommand;
	}

	void cancelKommands(RunnableActionTag tag) {
		if (tag != null) {
			for (RunnableAction runnableAction : removeCollection(tag)) {
				runnableAction.cancel();
			}
		}
	}

	private Collection<RunnableAction> removeCollection(RunnableActionTag key) {
		synchronized (currentRunnableActions) {
			return currentRunnableActions.removeAll(key);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		executor.shutdown();
	}

}
