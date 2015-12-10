package com.gagauz.tracker.db.utils;

import com.gagauz.tracker.utils.Serializer;

public class IntegerSerializer implements Serializer<Integer> {

    @Override
    public String serialize(Integer object) {
        return null != object ? object.toString() : "";
    }

    @Override
    public Integer unserialize(String string, Class<Integer> clazz) {
        return null == string || "".equals(string) ? null : Integer.parseInt(string);
    }

}
