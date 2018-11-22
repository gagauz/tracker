package com.gagauz.tracker.db.utils;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TimeTrackedEntity;

public class SaveOrUpdateDateListener extends EmptyInterceptor {
    private static final long serialVersionUID = -8760038504372041860L;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Ticket) {
            Ticket ticket = (Ticket) entity;
            if (null == ticket.getKey()) {
                setValue("key", ticket.getProject().getCode() + '-' + id, state, propertyNames);
                return true;
            }
        } else if (entity instanceof TimeTrackedEntity) {
            TimeTrackedEntity timeTrackedEntity = (TimeTrackedEntity) entity;
            Date date = new Date();
            if (timeTrackedEntity.getCreated() == null) {
                timeTrackedEntity.setCreated(date);
                setValue("created", date, state, propertyNames);
            }
            timeTrackedEntity.setUpdated(date);
            setValue("updated", date, state, propertyNames);
            return true;
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    private void setValue(String property, Object value, Object[] state, String[] propertyNames) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equalsIgnoreCase(property)) {
                state[i] = value;
                break;
            }
        }
    }
}