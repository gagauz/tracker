package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Version;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class TaskList {

    @Property
    private Task task;

    @Property(write = false)
    private Version version;

    @Inject
    private TaskDao taskDao;

    Object onActivate(Version version) {
        if (null == version) {
            return Index.class;
        }

        this.version = version;

        return null;
    }

    public List<Task> getTasks() {
        return taskDao.findByVersion(version);
    }

}
