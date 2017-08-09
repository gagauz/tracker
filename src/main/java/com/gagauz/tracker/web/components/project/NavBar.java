package com.gagauz.tracker.web.components.project;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.web.config.Global;

import com.gagauz.tracker.db.model.Project;

public class NavBar {
    @Property(write = false)
    private Project project;

    boolean setupRender() {
        this.project = Global.peek(Project.class);
        return null != this.project;
    }
}
