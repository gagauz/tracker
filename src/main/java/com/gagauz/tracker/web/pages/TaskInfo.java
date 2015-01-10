package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.annotations.Property;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.db.model.Task;

@Secured
public class TaskInfo {

    @Property(write = false)
    private Task task;

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
}
