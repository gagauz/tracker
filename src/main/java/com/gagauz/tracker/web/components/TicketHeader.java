package com.gagauz.tracker.web.components;

import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.WorkflowDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.services.ToolsService;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import java.util.Objects;

public class TicketHeader {
    @Component(parameters = {"id=literal:ticketZone"})
    private Zone ticketZone;

    @Parameter(required = true)
    @Property(write = false)
    private Ticket ticket;

    @Property
    private Ticket newTicket;

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
        newTicket.setFeatureVersion(ticket.getFeatureVersion());
        subtask = true;
        return newTicket;
    }

    public boolean isNotMe(User owner) {
        return !Objects.equals(user, owner);
    }
}
