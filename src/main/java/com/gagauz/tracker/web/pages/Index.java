package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.web.services.security.Secured;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured(Role.VIEWER)
public class Index {

    @Property
    private Project project;

    @Inject
    private ProjectDao projectDao;

    @Secured(Role.VIEWER)
    public List<Project> getProjects() {
        return projectDao.findAll();
    }

}
