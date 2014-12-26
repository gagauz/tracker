package com.gagauz.tracker.db.base;

public interface Function<P, R> {
    R call(P param);
}
