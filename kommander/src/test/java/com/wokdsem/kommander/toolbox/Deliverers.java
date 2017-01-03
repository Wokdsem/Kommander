package com.wokdsem.kommander.toolbox;

import com.wokdsem.kommander.Deliverer;

public class Deliverers {

	private Deliverers() {
	}

	public static Deliverer getDefaultDeliverer() {
		return new Deliverer() {
			@Override
			public void deliver(Runnable runnable) {
				runnable.run();
			}
		};
	}
	
}
