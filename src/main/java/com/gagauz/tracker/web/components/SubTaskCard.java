package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class SubTaskCard {
    @Parameter
    @Property
    private Task subTask;
}