package com.gagauz.tracker.web.pages;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.CanbanGroup;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.services.dao.CanbanGroupDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;

@Import(stack = "app")
@Secured
public class VersionCanban extends VersionInfo {
	@Component(parameters = { "id=prop:zoneId" })
	@Property
	private Zone zone;

	@Property
	private CanbanGroup newgroup;

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

	public SelectModel getModel() {
		return selectModelFactory.create(ticketStatusDao.findByProject(version.getProject()), "name");
	}

	void onSuccessFromForm1() {
		canbanGroupDao.save(newgroup);
	}

}
