package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Version;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDao extends AbstractDao<Task.TaskId, Task> {

    @SuppressWarnings("unchecked")
    public List<Task> findByVersion(Version version) {
        return getSession().createQuery("from Task t where version=:version").setEntity("version", version).list();
    }

}
