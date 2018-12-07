package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.model.CollectionGridDataSourceRowTypeFix;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.services.dao.ProjectDao;

public class Index {

    @Property
    private Project project;

    @Inject
    private ProjectDao projectDao;

    public GridDataSource getProjects() {
        return new CollectionGridDataSourceRowTypeFix<>(projectDao.findAll(), Project.class);
    }

}
