package com.gagauz.tracker.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMultimap<K, V, C extends Collection<V>> implements Multimap<K, V> {

    private Map<K, C> internalMap = new HashMap<K, C>();

    protected abstract C createCollection();

    @Override
    public Map<K, Collection<V>> asMap() {
        return null;
    }

    public V put(K key, V value) {
        return value;

    }
}
