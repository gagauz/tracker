package com.gagauz.tracker.db.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChildrenArrayList<P, E extends Child<P>> extends ArrayList<E> {
    private static final long serialVersionUID = -2771837665733989137L;

    private final P parent;

    public ChildrenArrayList(P parent, List<E> list) {
        super(null == list ? FactoryX.newArrayList() : list);
        this.parent = parent;
    }

    @Override
    public boolean add(E e) {
        e.setParent(parent);
        return super.add(e);
    }

    @Override
    public void add(int index, E e) {
        e.setParent(parent);
        super.add(index, e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            e.setParent(parent);
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            e.setParent(parent);
        }
        return super.addAll(index, c);
    }

    @Override
    public E remove(int index) {
        E e = super.remove(index);
        e.setParent(null);
        return e;
    };

    @Override
    public boolean remove(Object o) {
        if (o instanceof Child) {
            ((E) o).setParent(null);
        }
        return super.remove(o);
    };

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            if (o instanceof Child) {
                ((E) o).setParent(null);
            }
        }
        return super.removeAll(c);
    }
}
