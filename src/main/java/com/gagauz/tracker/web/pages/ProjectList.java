package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.db.model.Project;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class ProjectList {

    @Property
    private Project project;

    @Inject
    private ProjectDao projectDao;

    public List<Project> getProjects() {
        return projectDao.findAll();
    }

}
