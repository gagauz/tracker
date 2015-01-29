package com.gagauz.tracker.beans.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskStatus;
import com.gagauz.tracker.db.model.Version;

@Service
public class TaskDao extends AbstractDao<Integer, Task> {

    public List<Task> findByProject(Project project) {
        return getSession().createQuery("from Task t where version.project=:project").setEntity("project", project).list();
    }

    public List<Task> findByVersion(Version version) {
        return getSession().createQuery("from Task t where version=:version").setEntity("version", version).list();
    }

    public List<Task> findOpen(Version version) {
        return getSession().createQuery("from Task t where version=:version and status in (:status) order by t.progress/t.estimate desc priority desc")
                .setParameterList("status", Arrays.asList(TaskStatus.OPEN, TaskStatus.IN_PROGRESS))
                .setEntity("version", version)
                .list();
    }

    public void updateTaskProgessTime(Task task) {
        getSession().createSQLQuery("update task set progress=ifnull((select sum(logTime) from work_log where task_id="
                + task.getId() + " group by task_id), 0) where id=" + task.getId()).executeUpdate();
    }
}
