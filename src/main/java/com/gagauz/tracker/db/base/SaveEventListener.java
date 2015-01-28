package com.gagauz.tracker.db.base;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

import com.gagauz.tracker.db.model.WorkLog;

public class SaveEventListener implements SaveOrUpdateEventListener {

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        if (event.getEntity() instanceof WorkLog) {
            event.getSession().createSQLQuery(
                    "update task set progress=(select sum(logTime) from work_log where task_id=" + event.getResultId() + " group by task_id) where id="
                            + event.getResultId());

        }
    }

}
