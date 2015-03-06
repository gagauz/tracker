package com.gagauz.tracker.db.utils;

import com.gagauz.tracker.utils.Serializer;

public class StringSerializer implements Serializer<String> {

    @Override
    public String serialize(String object) {
        return object;
    }

    @Override
    public String unserialize(String string) {
        return string;
    }

}
