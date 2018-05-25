package com.gagauz.tracker.web.components.ticket;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.db.model.Attachment;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketComment;
import com.gagauz.tracker.services.dao.TicketCommentDao;

@Import(module = "bootstrap/collapse")
public class ViewTicketForm {

	@Parameter(name = "ticket")
	private Ticket ticketParam;

	@Parameter(value = "false")
	@Property
	private boolean popup;

	@Property
	private Ticket ticket;

	@Property
	private Attachment attachment;

	@Property
	private List<TicketComment> comments;

	@Property
	private TicketComment comment;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private TicketCommentDao ticketCommentDao;

	boolean setupRender() {
		if (null != ticketParam) {
			ticket = ticketParam;
		}
		return ticket != null;
	}

	public String getAjaxUrl() {
		return componentResources.createEventLink("getComments", ticket).toRedirectURI();
	}

	void onGetComments(Ticket ticket) {
		comments = ticketCommentDao.findByTicket(ticket);
	}
}
