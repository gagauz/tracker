package com.gagauz.tracker.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.web.services.ToolsService;

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
        return toolsService.getTime(task.getEstimate() - task.getProgress());
    }
}
