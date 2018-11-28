package com.gagauz.tracker.web.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.web.services.ToolsService;

import com.gagauz.tracker.db.model.CanbanGroup;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.CanbanGroupDao;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;
import com.gagauz.tracker.utils.ColorMap;
import com.xl0e.util.C;

@Import(module = "bootstrap/dropdown")
public class VersionStatusMap {

	@Parameter(allowNull = false, required = true, principal = true)
	private Version version;

	@Parameter
	private CanbanGroup group;

	@Component(parameters = { "id=literal:ticketZone", "show=popup", "update=popup" })
	private Zone ticketZone;

	@Component(parameters = { "id=prop:zoneId" })
	private Zone zone;

	@Property
	private Feature feature;

	@Property
	private TicketStatus status;

	private Ticket ticket;

	@Property
	private Ticket viewTicket;

	@Inject
	private FeatureDao featureDao;

	@Inject
	private TicketDao ticketDao;

	@Inject
	private TicketStatusDao ticketStatusDao;

	@Inject
	private Messages messages;

	@SessionState
	private User securityUser;

	@Inject
	private ComponentResources resources;

	@Inject
	private ToolsService toolsService;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private CanbanGroupDao canbanGroupDao;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	private Map<TicketStatus, List<Ticket>> statusToTicketsMap;

	boolean setupRender() {
		return group != null;
	}

	@Cached
	public Collection<TicketStatus> getStatuses() {

		if (null == this.statusToTicketsMap) {
			List<TicketStatus> statuses = C.arrayList(group.getStatuses());

			if (statuses.isEmpty()) {
				return Collections.emptyList();
			}
			Collections.sort(statuses, (a, b) -> {
				if (C.isEmpty(a.getAllowedTo())) {
					return 1;
				}
				if (a.getAllowedTo().contains(b)) {
					return -1;

				}
				if (b.getAllowedTo().contains(a)) {
					return 1;

				}
				if (!C.isEmpty(a.getAllowedTo())) {
					return -1;
				}
				return 0;
			});
			statusToTicketsMap = new LinkedHashMap<>(statuses.size());
			statuses.forEach(status -> {
				statusToTicketsMap.put(status, C.arrayList());
			});

			for (Ticket ticket : ticketDao.findByVersionAndStatuses(version, statuses)) {
				statusToTicketsMap.computeIfAbsent(ticket.getStatus(), x -> C.arrayList()).add(ticket);
			}
		}
		return statusToTicketsMap.keySet();
	}

	public Collection<Ticket> getStatusTickets() {
		return statusToTicketsMap.get(status);
	}

	public boolean isDraggable() {
		return true;
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getEventUrl() {
		return resources.createEventLink("change").toRedirectURI();
	}

	void afterRender() {
		javaScriptSupport.require("page/version_map").invoke("init").with(getEventUrl());
	}

	@Ajax
	void onChange(@RequestParameter(value = "target") Integer statusId,
			@RequestParameter(value = "ticket") Integer ticketId) {
		TicketStatus status = ticketStatusDao.findById(statusId);
		Ticket ticket = this.ticketDao.findById(ticketId);
		if (null != ticket) {
			ticket.setStatus(status);
			ticketDao.save(ticket);
			ajaxResponseRenderer.addRender(getZoneId(), zone.getBody());
		}
	}

	@Ajax
	void onViewTicket(Ticket ticket) {
		this.viewTicket = ticket;
		this.ajaxResponseRenderer
				.addRender(Layout.MODAL_BODY_ID, this.ticketZone.getBody())
				.addCallback(
						(JavaScriptCallback) javascriptSupport -> javascriptSupport.require("modal").invoke("showModal")
								.with(Layout.MODAL_ID));
	}

	public String getBgColor() {
		return ColorMap.getColor(status.getId());
	}

	public void onRemove(CanbanGroup group) {
		canbanGroupDao.delete(group);
	}

	public String getZoneId() {
		return getClass().getSimpleName() + "_zone";
	}

	public String getAllowTo(TicketStatus status) {
		return C.emptyIfNull(status.getAllowedTo()).stream()
				.map(TicketStatus::getId)
				.map(String::valueOf)
				.reduce(null,
						(a, b) -> a == null ? b : a + "," + b);
	}
}
