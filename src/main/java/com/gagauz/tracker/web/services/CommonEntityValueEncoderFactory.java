package com.gagauz.tracker.web.services;

import com.gagauz.tracker.beans.dao.AbstractDao;
import com.gagauz.tracker.db.base.Identifiable;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

public class CommonEntityValueEncoderFactory<E extends Identifiable, Dao extends AbstractDao<Integer, E>> implements
ValueEncoderFactory<E> {

    private static final String NULL = "null";

    private static boolean isNull(String arg0) {
        return null == arg0 || NULL.equals(arg0);
    }

    private final Dao dao;

    public CommonEntityValueEncoderFactory(Class entity) {
        this.dao = (Dao) AbstractDao.getDao(entity);
    }

    @Override
    public ValueEncoder<E> create(Class<E> type) {
        return new ValueEncoder<E>() {
            @Override
            public String toClient(E arg0) {
                return null == arg0 ? NULL : String.valueOf(arg0.getId());
            }

            @Override
            public E toValue(String arg0) {
                return isNull(arg0) ? null : dao.findById(Integer.parseInt(arg0));
            }
        };
    }

}
