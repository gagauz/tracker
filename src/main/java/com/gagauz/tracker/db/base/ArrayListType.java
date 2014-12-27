package com.gagauz.tracker.db.base;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListType extends CollectionType {

    @Override
    public Collection createCollection(Class class1, int size) {
        return new ArrayList(size);
    }
}
