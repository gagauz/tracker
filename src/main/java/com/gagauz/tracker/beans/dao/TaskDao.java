package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Version;

@Service
public class TaskDao extends AbstractDao<Integer, Task> {

    public List<Task> findByProject(Project project) {
        return getSession().createQuery("from Task t where version.project=:project").setEntity("project", project).list();
    }

    public List<Task> findByVersion(Version version) {
        return getSession().createQuery("from Task t where version=:version").setEntity("version", version).list();
    }

}
