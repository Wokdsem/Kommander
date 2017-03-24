package com.wokdsem.kommander;

import java.util.concurrent.Executor;

/**
 * {@code Kommander} is the entry point to the <b>Kommander</b> tool. You need to use a {@code Kommander} instance to build the key
 * component, the {@link Kommand}. Built Kommands will be executed on the {@link Executor} and delivered on the {@link Deliverer}
 * of {@code Kommander}.
 */
public class Kommander {
	
	private final Dispatcher dispatcher;
	
	private Kommander(Deliverer deliverer, Executor executor) {
		this.dispatcher = new Dispatcher(deliverer, executor);
	}
	
	/**
	 * Builds a new Kommander instance with the default Kommander's {@link Executor} on the given {@link Deliverer}.
	 *
	 * @return the {@code Kommander} instance
	 * @throws IllegalArgumentException when the given {@link Deliverer} is null
	 */
	public static Kommander getInstance(Deliverer deliverer) {
		return getInstance(deliverer, KommandExecutor.newInstance());
	}
	
	/**
	 * Builds a new Kommander instance with the given {@link Executor} and {@link Deliverer}.
	 *
	 * @return the {@code Kommander} instance
	 * @throws IllegalArgumentException when the given {@link Deliverer} is null
	 * @throws IllegalArgumentException when the given {@link Executor} is null
	 */
	public static Kommander getInstance(Deliverer deliverer, Executor executor) {
		if (deliverer == null) {
			throw new IllegalArgumentException("Illegal null deliverer to instantiate Kommander.");
		}
		if (executor == null) {
			throw new IllegalArgumentException("Illegal null executor to instantiate Kommander.");
		}
		return new Kommander(deliverer, executor);
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
