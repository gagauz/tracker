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
import org.apache.tapestry5.web.config.Global;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.StageDao;
import com.gagauz.tracker.services.dao.UserGroupDao;
import com.gagauz.tracker.services.dao.VersionDao;

@Secured
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
    private UserGroup roleGroup;

    @Property
    private Version version;

    @Property
    private Ticket ticket;

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
    private UserGroupDao roleGroupDao;

    @SessionState
    private User securityUser;

    Object onActivate(Project project) {
        if (null == project) {
            return Index.class;
        }
        Global.put(Project.class, project);
        this.project = project;
        Global.put(Project.class, project);
        return null;
    }

    Object onPassivate() {
        return this.project;
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

    Object onCreateVersion() {
        this.newVersion = new Version();
        Version lastVersion = this.versionDao.findLast(this.project);
        String nextName = getNextVersion(this.project, lastVersion);
        this.newVersion.setName(nextName);
        this.newVersion.setCvsBranchName(nextName);
        return this.newVersion;
    }

    void onSuccessFromVersionForm() {
        this.newVersion.setProject(this.project);
        this.versionDao.save(this.newVersion);
        this.newVersion = null;
    }

    Object onCreateFeature() {
        this.newFeature = new Feature();
        return this.newFeature;
    }

    void onSuccessFromFeatureForm() {
        this.newFeature.setCreator(this.securityUser);
        this.newFeature.setProject(this.project);
        this.featureDao.save(this.newFeature);
        this.newFeature = null;
    }

    public List<Feature> getUserStories() {
        return this.featureDao.findByProject(this.project);
    }

    public List<Version> getVersions() {
        return this.versionDao.findByProject(this.project);
    }

    public List<UserGroup> getRoleGroups() {
        return this.roleGroupDao.findByProject(this.project);
    }

    public static void main(String[] args) {
        String g = getNextVersion("фывфвыфвы-11.10");
        System.out.println(g);
    }
}
