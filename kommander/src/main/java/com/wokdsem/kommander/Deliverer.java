package com.wokdsem.kommander;

/**
 * A {@code Deliver} defines how and where the {@link Kommand}'s responses are delivered.
 */
public interface Deliverer {
	
	/**
	 * Delivers the {@link Kommand}'s response wrapped in a {@link Runnable} instance.
	 */
	void deliver(Runnable runnable);
	
}
