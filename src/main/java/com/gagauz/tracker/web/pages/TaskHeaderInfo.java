package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.BugDao;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskHeader;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class TaskHeaderInfo {

    @Property(write = false)
    private TaskHeader taskHeader;

    @Property
    private Task task;

    @Inject
    private BugDao bugDao;

    Object onActivate(TaskHeader taskHeader) {
        if (null == taskHeader) {
            return ProjectList.class;
        }
        this.taskHeader = taskHeader;

        System.out.println(taskHeader.getTasks().size());

        return null;
    }

    Object onPassivate() {
        return taskHeader;
    }

}
