package com.wokdsem.android.kommander.toolbox;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleKeyCollection<K, T> {

	private final Map<K, Set<T>> map;

	{
		map = new HashMap<>();
	}

	public void put(K key, T value) {
		Set<T> set = getSet(key);
		set.add(value);
	}

	private Set<T> getSet(K key) {
		Set<T> set = map.get(key);
		if (set == null) {
			set = new HashSet<>();
			map.put(key, set);
		}
		return set;
	}

	public Collection<T> removeAll(K key) {
		Set<T> removed = map.remove(key);
		return removed == null ? Collections.<T>emptySet() : removed;
	}

	public void removeValue(K key, T value) {
		Set<T> set = map.get(key);
		if (set != null) {
			set.remove(value);
			if (set.isEmpty()) map.remove(key);
		}
	}

}
