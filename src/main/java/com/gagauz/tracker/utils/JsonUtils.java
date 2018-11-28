package com.gagauz.tracker.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonUtils {
    public static Optional<?> parseMap(Map map, String path) {
        String[] paths = path.split("/");
        Object value = map;
        for (String p : paths) {
            value = ((Map) value).get(p);
            if (null == value) {
                break;
            }
        }
        return Optional.ofNullable(value);
    }

    public static Optional<Map> parseObject(Map map, String path) {
        return (Optional<Map>) parseMap(map, path);
    }

    public static Optional<Integer> parseInt(Map map, String path) {
        return (Optional<Integer>) parseMap(map, path);
    }

    public static Optional<List<Map>> parseArray(Map map, String path) {
        return (Optional<List<Map>>) parseMap(map, path);
    }

    public static Optional<String> parseString(Map map, String path) {
        return (Optional<String>) parseMap(map, path);
    }

}
