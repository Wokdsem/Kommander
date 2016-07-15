package com.wokdsem.kommander;

class KommandBundle<T> {

	final Action<T> action;
	final KommandDeliverer deliverer;
	private final long kommanderId;
	Response.OnCompleted<T> onCompleted;
	Response.OnError onError;
	String tag;

	public KommandBundle(Action<T> action, KommandDeliverer deliverer, long kommanderId) {
		this.action = action;
		this.deliverer = deliverer;
		this.kommanderId = kommanderId;
	}

	public KommandTag getKommandTag() {
		return new KommandTag(kommanderId, tag);
	}

}
