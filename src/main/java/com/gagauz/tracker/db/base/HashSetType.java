package com.gagauz.tracker.db.base;

import java.util.Collection;
import java.util.HashSet;

public class HashSetType<E> extends CollectionType<E> {

    @Override
    public Collection<E> createCollection(Class<E> class1, int size) {
        return new HashSet<E>(size);
    }
}
