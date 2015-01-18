package com.gagauz.tracker.utils;

public class Adapters {
    private Adapters() {
    }

    public static final Function<Enum<?>, String> ENUM_TO_STRING = new Function<Enum<?>, String>() {
        @Override
        public String call(Enum<?> p) {
            return p.name();
        }
    };

}
