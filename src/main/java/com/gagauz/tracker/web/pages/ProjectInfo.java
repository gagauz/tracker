package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.TaskHeaderDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TaskHeader;
import com.gagauz.tracker.db.model.Version;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class ProjectInfo {

    @Property
    private Project project;

    @Property
    private Version version;

    @Property
    private TaskHeader taskHeader;

    @Inject
    private TaskHeaderDao taskHeaderDao;

    @Inject
    private VersionDao versionDao;

    Object onActivate(Project project) {
        if (null == project) {
            return ProjectList.class;
        }
        this.project = project;
        return null;
    }

    Object onPassivate() {
        return project;
    }

    public List<TaskHeader> getTaskHeaders() {
        return taskHeaderDao.findByProject(project);
    }

    public List<Version> getVersions() {
        return versionDao.findByProject(project);
    }

}
