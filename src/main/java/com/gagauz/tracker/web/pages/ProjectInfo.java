package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class ProjectInfo {

    @Property
    private Project project;

    @Property
    private Version version;

    @Property
    private Task task;

    @Property
    private SubTask subtask;

    @Property
    private Feature feature;

    @Inject
    private FeatureDao featureDao;

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

    public List<Feature> getUserStories() {
        return featureDao.findByProject(project);
    }

    public List<Version> getVersions() {
        return versionDao.findByProject(project);
    }

}
