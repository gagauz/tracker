package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class TaskForm {
    @Parameter(name = "task")
    private Task taskParam;

    @Property(write = false)
    private Task task;

    boolean setupRender() {
        if (null != taskParam) {
            task = taskParam;
        }
        return task != null;
    }
}
