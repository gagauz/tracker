package com.gagauz.tracker.web.pages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.gagauz.tracker.web.security.Secured;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;

@Secured({AccessRole.PROJECT_USER, AccessRole.PROJECT_ADMIN})
public class ProjectInfo {

    @Component(parameters = { "id=literal:createVersionZone" })
    @Property(write = false)
    private Zone modalZone;

    @Property
    private Project project;

    @Property
    @Persist("flash")
    private Version newVersion;

    @Property
    @Persist("flash")
    private Feature newFeature;

    @Property
    @Persist("flash")
    private Stage newStage;

    @Property
    private RoleGroup roleGroup;

    @Property
    private Version version;

    @Property
    private FeatureVersion ticket;

    @Property
    private Ticket subticket;

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

    @SessionState
    private User securityUser;

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

    private static String getNextVersion(Project project, Version lastVersion) {
        if (null != lastVersion) {
            String name = getNextVersion(lastVersion.getName());
            if (null != name) {
                return name;
            }
        }
        return project.getCode() + "-1";
    }

    private static String getNextVersion(String lastVersion) {
        String name = lastVersion;
        Pattern p = Pattern.compile("^(.*?)([0-9]+)$");
        Matcher m = p.matcher(name);
        if (m.find()) {
            String v = m.group(2);
            v = String.valueOf(NumberUtils.toInt(v) + 1);
            return m.replaceFirst("$1" + v);
        }
        return null;
    }

    void onCreateVersion() {
        newVersion = new Version();
        Version lastVersion = versionDao.findLast(project);
        String nextName = getNextVersion(project, lastVersion);
        newVersion.setName(nextName);
        newVersion.setCvsBranchName(nextName);
    }

    void onSuccessFromVersionForm() {
        newVersion.setProject(project);
        versionDao.save(newVersion);
        newVersion = null;
    }

    void onCreateFeature() {
        newFeature = new Feature();
    }

    void onSuccessFromFeatureForm() {
        newFeature.setCreator(securityUser);
        newFeature.setProject(project);
        featureDao.save(newFeature);
        newFeature = null;
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

    public static void main(String[] args) {
        String g = getNextVersion("фывфвыфвы-11.10");
        System.out.println(g);
    }
}
