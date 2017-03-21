package com.wokdsem.kommander;

import java.lang.ref.WeakReference;

class KommandTokens {
	
	private KommandTokens() {
	}
	
	static KommandToken newSimpleKommandToken(final Cancelable cancelable) {
		return new KommandToken() {
			@Override
			public void cancel() {
				cancelable.cancel();
			}
		};
	}
	
	static KommandToken newWeakKommandToken(Cancelable cancelable) {
		return new WeakKommandToken(cancelable);
	}
	
	private static class WeakKommandToken implements KommandToken {
		
		private final WeakReference<Cancelable> actionReference;
		
		private WeakKommandToken(Cancelable cancelable) {
			this.actionReference = new WeakReference<>(cancelable);
		}
		
		@Override
		public void cancel() {
			Cancelable cancelable = actionReference.get();
			if (cancelable != null) {
				cancelable.cancel();
			}
		}
	}
	
}
