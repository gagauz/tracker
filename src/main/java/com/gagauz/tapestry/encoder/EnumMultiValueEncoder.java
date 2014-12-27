package com.gagauz.tapestry.encoder;

import java.util.Collection;
import java.util.EnumSet;

public class EnumMultiValueEncoder<E extends Enum<E>> extends AbstractCollectionValueEncoder<E> {

    private final Class<E> enumClass;

    public EnumMultiValueEncoder(Class<E> clazz) {
        enumClass = clazz;
    }

    @Override
    public String toClient(E value) {
        return value.name();
    }

    @Override
    public E toValue(String clientValue) {
        try {
            E value = Enum.valueOf(enumClass, clientValue);
            return value;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected Collection<E> createCollection(int size) {
        return EnumSet.noneOf(enumClass);
    }
}
