package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Bug;
import com.gagauz.tracker.db.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugDao extends AbstractDao<Integer, Bug> {
    @SuppressWarnings("unchecked")
    public List<Bug> findByTask(Task task) {
        return getSession().createQuery("from Bug b where task=:task").setEntity("task", task).list();
    }

}
