package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.services.dao.*;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ResolveTicketForm {
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
    private SelectModelFactory selectModelFactory;

    @Inject
    private TicketStatusDao ticketStatusDao;

    @Inject
    private Request request;

    @SessionState
    private User user;

    public String getZoneId() {
        return zone.getClientId();
    }

    Object onSubmitFromForm() {
        System.out.println(ticket.getStatus());
        System.out.println(assign.getToStatus());
        if (!form.getHasErrors() && !Objects.equals(ticket.getStatus(), assign.getToStatus())) {
            assign.setFromStatus(ticket.getStatus());
            assign.setAuthor(user);
            ticket.setStatus(assign.getToStatus());
            ticketDao.merge(ticket);
            assign.setTicket(ticket);
            workflowDao.save(assign);
        }
        return request.isXHR() ? zone.getBody() : null;
    }

    @Cached
    public SelectModel getStatusModel() {
        System.out.println(ticket);
        List<TicketStatus> list = Collections.emptyList();
        if (null != ticket) {
            list = ticketStatusDao.findByProject(ticket.getFeature().getProject());
            final TicketStatus from = ticket.getStatus();
            list = list.stream().filter(new Predicate<TicketStatus>() {
                @Override
                public boolean test(TicketStatus t) {
                    return !t.equals(from);
                }
            }).collect(Collectors.<TicketStatus>toList());
        }
        return selectModelFactory.create(list, "name");
    }

}
