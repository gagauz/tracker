package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.annotation.PageContext;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.TicketDao;

@Secured
public class VersionInfo {

	@PageContext(index = 0)
	@Property(write = false)
	protected Version version;

	@Property
	private Ticket ticket;

	@Inject
	private TicketDao ticketDao;

	public Object onActivate() {
		if (null == version) {
			return Index.class;
		}

		return null;
	}

	@Cached
	public List<Ticket> getAllTickets() {
		return ticketDao.findByVersion(version);
	}

	@Cached
	public List<Ticket> getTickets() {
		return getAllTickets();
	}

	@Cached
	public List<Ticket> getBugs() {
		return getAllTickets();
	}
}
