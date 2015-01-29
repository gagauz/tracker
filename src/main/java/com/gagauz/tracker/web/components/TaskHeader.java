package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.web.services.ToolsService;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class TaskHeader {
    @Parameter(required = true)
    @Property(write = false)
    private Task task;

    @Inject
    protected ToolsService toolsService;

    public String getTime(int min) {
        return toolsService.getTime(min);
    }

    public String getRemaining() {
        return toolsService.getTime(task.getEstimated() - task.getProgress());
    }
}
