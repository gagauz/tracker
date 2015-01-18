package com.gagauz.tracker.utils;

import java.util.ArrayList;
import java.util.Collection;

public class AdapterUtils {
    private AdapterUtils() {
    }

    public static <P, V> Collection<V> transform(Collection<P> iterable, Function<P, V> adapter) {
        Collection<V> result = new ArrayList<V>(iterable.size());
        for (P p : iterable) {
            result.add(adapter.call(p));
        }
        return result;
    }

    public static <P, V> Collection<V> transform(P[] iterable, Function<P, V> adapter) {
        ArrayList<V> result = new ArrayList<V>(iterable.length);
        for (P p : iterable) {
            result.add(adapter.call(p));
        }
        return result;
    }
}
