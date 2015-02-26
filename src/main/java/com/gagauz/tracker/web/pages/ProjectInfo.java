package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.RoleGroupDao;
import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Secured({Role.PROJECT_USER, Role.PROJECT_ADMIN})
public class ProjectInfo {

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
    private SecurityUser securityUser;

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

    private static String getNextVersion(String lastVersion) {
        String name = lastVersion;
        Pattern p = Pattern.compile("^.*[^0-9]([0-9]+)$");
        Matcher m = p.matcher(name);
        if (m.find()) {
            String v = m.group(1);
            return String.valueOf(NumberUtils.toInt(v) + 1);
        }
        return "1";
    }

    void onCreateVersion() {
        newVersion = new Version();
        Version lastVersion = versionDao.findLast(project);
        String versionName = project.getKey1() + '-' + getNextVersion(null == lastVersion ? "1" : lastVersion.getName());
        //        newVersion.setBranch();
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
        newFeature.setCreator((User) securityUser);
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
        String g = getNextVersion("PROJECT-1.10");
        System.out.println(g);
    }
}
