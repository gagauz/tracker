package com.gagauz.tracker.db.base;

public interface Child<P> {
    P getParent();

    void setParent(P p);
}
