package com.wokdsem.kommander;

class Deliverers {
	
	static Deliverer DEFAULT_DELIVERER;
	
	static {
		DEFAULT_DELIVERER = new Deliverer() {
			@Override
			public void deliver(Runnable runnable) {
				runnable.run();
			}
		};
	}
	
	private Deliverers() {
	}
	
}
