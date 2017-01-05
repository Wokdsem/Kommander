package com.wokdsem.kommander;

public interface Action<T> {

	T action() throws Throwable;

}
