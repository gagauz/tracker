package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.SubTask;
import com.gagauz.tracker.db.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTaskDao extends AbstractDao<Integer, SubTask> {

    @SuppressWarnings("unchecked")
    public List<SubTask> findByTask(Task task) {
        return getSession().createQuery("from SubTask t where task=:task").setEntity("task", task).list();
    }

}
