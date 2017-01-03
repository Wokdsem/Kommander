package com.wokdsem.kommander;

public interface Action<T> {

	T act() throws Throwable;

}
