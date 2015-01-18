package com.gagauz.tracker.utils;

import java.util.Collection;
import java.util.Map;

public interface Multimap<K, V> extends Map<K, Collection<V>> {

    V getFirst(K key);

    Map<K, Collection<V>> asMap();
}
