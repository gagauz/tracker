package com.gagauz.tracker.utils;

public interface Transformer<P, R> {
    R applyP(P param);

    P applyR(R param);
}
