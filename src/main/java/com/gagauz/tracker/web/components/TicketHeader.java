package com.gagauz.tracker.web.components;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.web.services.ToolsService;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;
import com.gagauz.tracker.services.dao.TicketTypeDao;
import com.gagauz.tracker.services.dao.VersionDao;
import com.gagauz.tracker.services.dao.WorkflowDao;

@Import(module = "bootstrap/dropdown")
public class TicketHeader {
    @Component(parameters = { "id=literal:ticketZone" })
    @Property(write = false)
    private Zone ticketZone;

    @Component(parameters = { "id=literal:headerZone" })
    @Property(write = false)
    private Zone headerZone;

    @Parameter(required = true)
    @Property(write = false)
    private Ticket ticket;

    @Property
    private Ticket newTicket;

    @Property
    private TicketStatus status;

    @Property
    private TicketType type;

    @Property
    private Version version;

    @Property
    private Feature feature;

    @Property
    private boolean edit;

    @Property
    private boolean subtask;

    @Property
    private boolean assign;

    @Property
    private boolean resolve;

    @Property
    private boolean attach;

    @Inject
    protected ToolsService toolsService;

    @Inject
    protected WorkflowDao workflowDao;

    @Inject
    protected TicketDao ticketDao;

    @Inject
    protected TicketStatusDao ticketStatusDao;

    @Inject
    protected TicketTypeDao ticketTypeDao;

    @Inject
    protected VersionDao versionDao;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @SessionState
    private User user;

    public String getTime(int min) {
        return toolsService.getTime(min);
    }

    public String getRemaining() {
        return toolsService.getTime(ticket.getEstimate() - ticket.getProgress());
    }

    @Ajax
    Object onEdit(Ticket ticket) {
        newTicket = ticket;
        edit = true;
        return ticket;
    }

    @Ajax
    Object onAssign(Ticket ticket) {
        newTicket = ticket;
        assign = true;
        return ticket;
    }

    @Ajax
    void onChangeType(Ticket ticket, TicketType type) {
        if (null != ticket && null != type) {
            ticket.setType(type);
            this.ticket = ticket;
        }
    }

    @Ajax
    Object onChangeStatus(Ticket ticket, TicketStatus status) {
        if (null != ticket && null != status) {
            ticket.setStatus(status);
            this.ticket = ticket;
        }
        return headerZone;
    }

    @Ajax
    Object onChangeVersion(Ticket ticket, Feature feature, Version version) {
        if (null != ticket && null != version) {
            ticket.setFeature(feature);
            ticket.setVersion(version);
            this.ticket = ticket;
        }
        return headerZone;
    }

    void onAssignToMe(Ticket ticket) {
        ticket.setOwner(user);
        ticketDao.save(ticket);
    }

    @Ajax
    Object onResolve(Ticket ticket) {
        newTicket = ticket;
        resolve = true;
        return ticket;
    }

    @Ajax
    Object onAttach(Ticket ticket) {
        newTicket = ticket;
        attach = true;
        return ticket;
    }

    @Ajax
    Object onSubtask(Ticket ticket) {
        newTicket = new Ticket();
        newTicket.setParent(ticket);
        newTicket.setAuthor(user);
        newTicket.setFeature(ticket.getFeature());
        newTicket.setVersion(ticket.getVersion());
        subtask = true;
        return newTicket;
    }

    public boolean isNotMe(User owner) {
        return !Objects.equals(user, owner);
    }

    public List<TicketType> getTypes() {
        return ticketTypeDao.findByUserGroupsAndProject(user.getUserGroups(), ticket.getProject());
    }

    public List<Version> getVersions() {
        return versionDao.findByFeature(ticket.getFeature());
    }

    @Cached
    public List<TicketStatus> getStatuses() {
        List<TicketStatus> list = ticketStatusDao.findByUserGroupsAndProject(user.getUserGroups(), ticket.getFeature().getProject());
        return list.stream()
                .filter(s -> Optional.ofNullable(ticket.getStatus())
                        .map(TicketStatus::getAllowedTo)
                        .map(c -> c.contains(s))
                        .orElse(true))
                .collect(Collectors.toList());
    }

}
