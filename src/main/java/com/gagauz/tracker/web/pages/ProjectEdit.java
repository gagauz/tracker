package com.gagauz.tracker.web.pages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.gagauz.tapestry.web.config.Global;
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

@Secured({ AccessRole.PROJECT_USER, AccessRole.PROJECT_ADMIN })
public class ProjectEdit {

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

	@Inject
	private ComponentResources resources;

	@SessionState
	private User securityUser;

	@Property
	private Block operation;

	Object onActivate(EventContext ctx) {
		if (ctx.getCount() > 0) {
			this.project = ctx.get(Project.class, 0);
			if (null == this.project) {
				return Index.class;
			}
			Global.put(Project.class, this.project);
			if (ctx.getCount() > 1) {

				try {
					this.operation = this.resources.getBlock(ctx.get(String.class, 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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

	public List<RoleGroup> getRoleGroups() {
		return this.roleGroupDao.findByProject(this.project);
	}

	public static void main(String[] args) {
		String g = getNextVersion("фывфвыфвы-11.10");
		System.out.println(g);
	}
}
