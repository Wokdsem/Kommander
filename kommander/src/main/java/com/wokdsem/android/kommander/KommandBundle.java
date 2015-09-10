package com.wokdsem.android.kommander;

class KommandBundle<T> {

	final Action<T> action;
	Response.OnCompleted<T> onCompleted;
	Response.OnError onError;
	String tag;

	public KommandBundle(Action<T> action) {
		this.action = action;
	}

}
