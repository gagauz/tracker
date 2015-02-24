package com.gagauz.tracker.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> newArrayList() {
        return new ArrayList<T>();
    }

    public static <T> List<T> newArrayList(int size) {
        return new ArrayList<T>(size);
    }
}
