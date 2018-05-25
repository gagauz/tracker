package com.gagauz.tracker.utils;

public class ColorMap {
    static String[] COLORS = {
            "#f0f0ff",
            "#fff0f0",
            "#f0fff0",
            "#fff0ff",
            "#f0ffff",
            "#fffff0",
            "#ddd0d0",
            "#d0ddd0",
            "#d0d0dd",
            "#ddd0dd",
            "#d0dddd",
            "#ddddd0"
    };

    public static String getColor(int id) {
        return COLORS[id % COLORS.length];
    }
}
