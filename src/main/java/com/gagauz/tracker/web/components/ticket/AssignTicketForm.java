package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.beans.dao.TicketCommentDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.WorkflowDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Workflow;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AssignTicketForm {
    @Component
    private Zone zone;

    @Component
    private BeanEditForm form;

    @Parameter
    @Property
    private Ticket ticket;

    @Property
    private Workflow assign;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private TicketCommentDao ticketCommentDao;

    @Inject
    private UserDao userDao;

    @Inject
    private WorkflowDao workflowDao;

    @Inject
    private Request request;

    @SessionState
    private User user;

    public String getZoneId() {
        return zone.getClientId();
    }

    Object onSubmitFromForm() {
        if (!form.getHasErrors() && ticket.getOwner() != assign.getToOwner()) {
            assign.setFromOwner(ticket.getOwner());
            assign.setAuthor(user);
            ticket.setOwner(assign.getToOwner());
            ticketDao.merge(ticket);
            assign.setTicket(ticket);
            workflowDao.save(assign);
        }
        return request.isXHR() ? zone.getBody() : null;
    }

    public User getUser() {
        return user;
    }
}
