package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured({Role.PROJECT_USER, Role.PROJECT_ADMIN})
public class ProjectInfo {

    @Property
    private Project project;

    @Property
    @Persist("flash")
    private Version newVersion;

    @Property
    @Persist("flash")
    private Stage newStage;

    @Property
    private RoleGroup roleGroup;

    @Property
    private Version version;

    @Property
    private FeatureVersion task;

    @Property
    private Task subtask;

    @Property
    private Feature feature;

    @Property
    private Stage stage;

    @Inject
    private FeatureDao featureDao;

    @Inject
    private VersionDao versionDao;

    @Inject
    private StageDao stageDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    Object onActivate(Project project) {
        if (null == project) {
            return Index.class;
        }
        this.project = project;
        return null;
    }

    Object onPassivate() {
        return project;
    }

    void onCreateVersion() {
        newVersion = new Version();
        newVersion.setProject(project);
    }

    public List<Feature> getUserStories() {
        return featureDao.findByProject(project);
    }

    public List<Version> getVersions() {
        return versionDao.findByProject(project);
    }

    public List<RoleGroup> getRoleGroups() {
        return roleGroupDao.findByProject(project);
    }

}
