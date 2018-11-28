package com.gagauz.tracker.web.pages;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.web.services.annotation.PageContext;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.CanbanGroup;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.CanbanGroupDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;

@Import(stack = "app")
@Secured
public class VersionCanban {

	@Component(parameters = { "id=prop:zoneId" })
	@Property
	private Zone zone;

	@Property
	private CanbanGroup newgroup;

	@Property
	private CanbanGroup group;

	@PageContext(index = 0)
	@Property(write = false)
	protected Version version;

	@PageContext(index = 1)
	@Property(write = false)
	private CanbanGroup canbanGroup;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private TicketStatusDao ticketStatusDao;

	@Inject
	private CanbanGroupDao canbanGroupDao;

	public String getZoneId() {
		return "fasfsdfasdf";
	}

	Object onCreateGroup(Project project) {
		newgroup = new CanbanGroup();
		newgroup.setProject(project);
		return project;
	}

	Object onEditGroup(CanbanGroup g) {
		newgroup = g;
		return g;
	}

	void onRemoveGroup(CanbanGroup g) {
		canbanGroupDao.delete(g);
	}

	public SelectModel getModel() {
		return selectModelFactory.create(ticketStatusDao.findByProject(version.getProject()), "name");
	}

	void onSuccessFromForm1() {
		if (null != newgroup.getId()) {
			canbanGroupDao.merge(newgroup);
		} else {
			canbanGroupDao.save(newgroup);
		}
	}

	public List<CanbanGroup> getGroups() {
		return canbanGroupDao.findByProject(version.getProject());
	}

	void onSelectGroup(CanbanGroup group) {
		this.canbanGroup = group;
	}

	public boolean isSelectedGroup() {
		return Objects.equals(canbanGroup, group);
	}

}
