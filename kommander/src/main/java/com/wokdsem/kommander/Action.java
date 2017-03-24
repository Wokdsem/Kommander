package com.wokdsem.kommander;

/**
 * An {@code Action} defines a task, to be executed in the future, that returns a result and can throws an exception.
 *
 * @param <T> the result type of the {@code Action}.
 */
public interface Action<T> {
	
	T action() throws Throwable;
	
}
