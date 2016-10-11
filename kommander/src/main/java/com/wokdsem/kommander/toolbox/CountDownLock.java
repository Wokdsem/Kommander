package com.wokdsem.kommander.toolbox;

public class CountDownLock {

	private int count;
	private boolean released;

	public CountDownLock(int count) {
		this.count = Math.max(0, count);
	}

	public synchronized boolean await() throws InterruptedException {
		if (isLockAlive()) {
			wait();
		}
		return !released;
	}

	public synchronized void countDown() {
		if (isLockAlive()) {
			if (--count == 0) {
				notifyAll();
			}
		}
	}

	public synchronized void releaseLock() {
		if (isLockAlive()) {
			released = true;
			notifyAll();
		}
	}

	private boolean isLockAlive() {
		return !(released || count == 0);
	}

}
