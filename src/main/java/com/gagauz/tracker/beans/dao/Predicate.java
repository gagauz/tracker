package com.gagauz.tracker.beans.dao;

public interface Predicate<T> {
    boolean apply(T element);
}
