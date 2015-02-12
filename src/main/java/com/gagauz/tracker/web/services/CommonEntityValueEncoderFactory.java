package com.gagauz.tracker.web.services;

import com.gagauz.tracker.beans.dao.AbstractDao;
import com.gagauz.tracker.db.base.Identifiable;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

public class CommonEntityValueEncoderFactory<Entity extends Identifiable, Dao extends AbstractDao<Integer, Entity>> implements
        ValueEncoderFactory<Entity> {

    private static final String NULL = "null";

    private static boolean isNull(String arg0) {
        return null == arg0 || NULL.equals(arg0);
    }

    private final Dao dao;

    public CommonEntityValueEncoderFactory(Dao dao) {
        this.dao = dao;
    }

    @Override
    public ValueEncoder<Entity> create(Class<Entity> type) {
        return new ValueEncoder<Entity>() {
            @Override
            public String toClient(Entity arg0) {
                return null == arg0 ? NULL : String.valueOf(arg0.getId());
            }

            @Override
            public Entity toValue(String arg0) {
                return isNull(arg0) ? null : dao.findById(Integer.parseInt(arg0));
            }
        };
    }

}
