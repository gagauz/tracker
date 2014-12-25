package com.gagauz.tracker.utils;

public interface TextTransformer<P> extends Transformer<P, String> {
    @Override
    String applyP(P param);

    @Override
    P applyR(String param);
}
