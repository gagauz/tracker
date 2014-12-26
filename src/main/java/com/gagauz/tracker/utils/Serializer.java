package com.gagauz.tracker.utils;

public interface Serializer<P> {
    String serialize(P object);

    P unserialize(String string);
}
