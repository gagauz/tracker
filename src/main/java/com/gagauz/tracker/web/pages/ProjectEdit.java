package com.gagauz.tracker.web.pages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.PageActivationContext;
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
import com.gagauz.tracker.db.model.cvs.ProjectRepository;
import com.gagauz.tracker.db.model.cvs.Repository;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.StageDao;
import com.gagauz.tracker.services.dao.UserGroupDao;
import com.gagauz.tracker.services.dao.VersionDao;
import com.gagauz.tracker.services.dao.cvs.RepositoryDao;

@Secured
public class ProjectEdit {

    @Component(parameters = { "id=literal:createVersionZone" })
    @Property(write = false)
    private Zone modalZone;

    @Property
    @PageActivationContext(index = 0)
    private Project project;

    @Property
    @PageActivationContext(index = 2)
    private Object object;

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

    @Property
    private Block operation;

    @Property
    private ProjectRepository newProjectRepository;

    @Property
    private Repository newRepository;

    @Inject
    private FeatureDao featureDao;

    @Inject
    private VersionDao versionDao;

    @Inject
    private StageDao stageDao;

    @Inject
    private UserGroupDao roleGroupDao;

    @Inject
    private RepositoryDao repositoryDao;

    @Inject
    private ProjectDao projectDao;

    @Inject
    private ComponentResources resources;

    @SessionState
    private User securityUser;

    @PageActivationContext(index = 1)
    private String operationName;

    Object onActivate(EventContext ctx) {
        if (null == this.project) {
            return Index.class;
        }

        Global.put(Project.class, this.project);

        try {
            operationName = ctx.get(String.class, 1);
            this.operation = this.resources.getBlock(operationName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        newProjectRepository = project.getProjectRepository();
        if (null != newProjectRepository) {
            newRepository = newProjectRepository.getRepository();
        }
        return null;
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
        return this.newVersion;
    }

    Object onCreateFeature() {
        this.newFeature = new Feature();
        return this.newFeature;
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

    void onSuccessFromNewRepository() {
        repositoryDao.save(newRepository);
    }

    void onSuccessFromNewProjectRepository() {
        project.setProjectRepository(newProjectRepository);
        projectDao.save(project);
    }
}
