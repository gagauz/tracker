package com.gagauz.tracker.db.base;

import java.util.Collection;
import java.util.EnumSet;

public class EnumSetType extends CollectionType {

    @Override
    public Collection createCollection(Class class1, int size) {
        return EnumSet.noneOf(class1);
    }
}
