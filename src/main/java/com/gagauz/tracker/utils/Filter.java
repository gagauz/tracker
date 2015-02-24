package com.gagauz.tracker.utils;

import com.gagauz.tracker.beans.dao.Predicate;

import java.util.Collection;
import java.util.List;

public class Filter {
    private Filter() {
    }

    public static <E> List<E> filter(Collection<E> collection, Predicate<E> predicate) {
        List<E> result = CollectionUtils.newArrayList(collection.size());
        for (E element : collection) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
}
