package com.wokdsem.kommander;

public interface Response {

	interface OnCompleted<T> {
		void onCompleted(T response);
	}

	interface OnError {
		void onError(Throwable e);
	}

}
