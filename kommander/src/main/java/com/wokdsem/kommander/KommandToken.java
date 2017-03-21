package com.wokdsem.kommander;

/**
 * Enables a way to cancel the previous launched {@code Kommand}.
 * The Kommand's {@link Action} is not executed if the computation has not started yet, try to stop the execution when
 * it is running, or at least, the execution is not delivered when that is finished.
 */
public interface KommandToken {
	
	/**
	 * Requests the canceling the command execution.
	 */
	void cancel();
	
}
