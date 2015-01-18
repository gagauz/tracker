package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskStatus;
import com.gagauz.tracker.db.model.Version;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TaskDao extends AbstractDao<Integer, Task> {

    public List<Task> findByProject(Project project) {
        return getSession().createQuery("from Task t where version.project=:project").setEntity("project", project).list();
    }

    public List<Task> findByVersion(Version version) {
        return getSession().createQuery("from Task t where version=:version").setEntity("version", version).list();
    }

    public List<Task> findOpen(Version version) {
        return getSession().createQuery("from Task t where version=:version and status in (:status) order by t.progress/t.estimated desc priority desc")
                .setParameterList("status", Arrays.asList(TaskStatus.OPEN, TaskStatus.IN_PROGRESS))
                .setEntity("version", version)
                .list();
    }

}
