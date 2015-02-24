package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Project;

import org.apache.tapestry5.annotations.Property;

import org.apache.tapestry5.annotations.Parameter;

public class ProjectMenu {
    @Parameter(autoconnect = true)
    @Property
    private Project project;
}
