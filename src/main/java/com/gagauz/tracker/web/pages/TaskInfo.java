package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.BugDao;
import com.gagauz.tracker.db.model.Bug;
import com.gagauz.tracker.db.model.SubTask;
import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class TaskInfo {

    @Property(write = false)
    private Task task;

    @Property
    private SubTask subTask;

    @Property
    private Bug bug;

    @Inject
    private BugDao bugDao;

    Object onActivate(Task task) {
        if (null == task) {
            return Index.class;
        }
        this.task = task;
        return null;
    }

    Object onPassivate() {
        return task;
    }

    public List<Bug> getBugs() {
        return bugDao.findByTask(task);
    }

}
