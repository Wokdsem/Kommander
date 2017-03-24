package com.wokdsem.kommander;

/**
 * Enables the way to receive the result, successful or erroneous, of the execution of a {@link Kommand}.
 */
public interface Response {
	
	/**
	 * A functional interface (callback) to receive the successful response of the execution of a {@link Kommand}
	 *
	 * @param <T> the response type
	 */
	interface OnCompleted<T> {
		
		/**
		 * Delivers the response of a kommand execution
		 *
		 * @param response the delivered value
		 */
		void onCompleted(T response);
		
	}
	
	/**
	 * A functional interface (callback) to receive an erroneous response of the execution of a {@link Kommand}
	 */
	interface OnError {
		
		/**
		 * Delivers the cause that failed in the kommand execution
		 *
		 * @param e the fail reason
		 */
		void onError(Throwable e);
		
	}
	
}
