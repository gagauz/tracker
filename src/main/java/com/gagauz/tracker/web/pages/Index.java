package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.services.dao.ProjectDao;

public class Index {

    @Property
    private Project project;

    @Inject
    private ProjectDao projectDao;

    public List<Project> getProjects() {
        return projectDao.findAll();
    }

}
