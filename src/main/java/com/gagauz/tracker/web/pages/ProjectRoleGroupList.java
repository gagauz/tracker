package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.db.model.Project;
import org.apache.tapestry5.annotations.Property;

public class ProjectRoleGroupList extends RoleGroupList {

    @Property
    private Project project;

    @Override
    Object onPassivate() {
        return new Object[] {project};
    }
}
