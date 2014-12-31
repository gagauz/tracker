package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Role;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.web.components.forms.VersionForm;
import com.gagauz.tracker.web.services.security.Secured;

@Secured({Role.PROJECT_USER, Role.PROJECT_ADMIN})
public class ProjectInfo {

    @Component
    private VersionForm versionForm;

    @Property
    private Project project;

    @Property
    @Persist("flash")
    private Version newVersion;

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

    @Inject
    private FeatureDao featureDao;

    @Inject
    private VersionDao versionDao;

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
