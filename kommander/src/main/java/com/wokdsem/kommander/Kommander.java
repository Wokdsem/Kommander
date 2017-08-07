package com.wokdsem.kommander;

import java.util.concurrent.Executor;

/**
 * {@code Kommander} is the entry point to the <b>Kommander</b> tool. You need to use a {@code Kommander} instance to build the key
 * component, the {@link Kommand}.
 */
public class Kommander {
	
	private final Dispatcher dispatcher;
	
	private Kommander(Executor executor) {
		this.dispatcher = new Dispatcher(executor);
	}
	
	/**
	 * Builds a new Kommander instance with the default Kommander's {@link Executor}.
	 *
	 * @return the {@code Kommander} instance
	 */
	public static Kommander getInstance() {
		return getInstance(KommandExecutor.newInstance());
	}
	
	/**
	 * Builds a new Kommander instance with the given {@link Executor}.
	 *
	 * @return the {@code Kommander} instance
	 * @throws IllegalArgumentException when the given {@link Executor} is null
	 */
	public static Kommander getInstance(Executor executor) {
		if (executor == null) {
			throw new IllegalArgumentException("Illegal null executor to instantiate Kommander.");
		}
		return new Kommander(executor);
	}
	
	/**
	 * Builds a {@link Kommand} bound to this {@code Kommander}.
	 *
	 * @param action value that defines the {@link Action} of the {@link Kommand}
	 * @param <T> the type of the {@link Kommand}
	 * @return a {@link Kommand} bound to this {@code Kommander}
	 */
	public <T> Kommand<T> makeKommand(Action<T> action) {
		return new Kommand<>(action, dispatcher);
	}
	
}
