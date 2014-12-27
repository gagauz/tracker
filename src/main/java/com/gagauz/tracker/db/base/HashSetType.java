package com.gagauz.tracker.db.base;

import java.util.Collection;
import java.util.HashSet;

public class HashSetType extends CollectionType {

    @Override
    public Collection createCollection(Class class1, int size) {
        return new HashSet(size);
    }
}
