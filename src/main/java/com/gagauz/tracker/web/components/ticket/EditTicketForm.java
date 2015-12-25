package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.dao.TicketTypeDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

import java.util.List;

public class EditTicketForm {

    @Component
    private Zone zone;

    @Component
    private BeanEditForm form;

    @Parameter(name = "ticket")
    private Ticket ticketParam;

    @Persist("flash")
    private Ticket ticket;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private TicketStatusDao ticketStatusDao;

    @Inject
    private TicketTypeDao ticketTypeDao;

    @Inject
    private UserDao userDao;

    @Inject
    private TicketStatusDao statusDao;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    @Inject
    private SelectModelFactory selectModelFactory;

    @Inject
    private Request request;

    @SessionState
    private User user;

    public String getZoneId() {
        return zone.getClientId();
    }

    Object onSubmitFromForm() {
        if (!form.getHasErrors()) {
            if (null == getTicket().getStatus()) {
                List<TicketStatus> status = statusDao.findByProject(getTicket().getProject());
                if (status.isEmpty()) {
                    status = statusDao.findCommon();
                }
                getTicket().setStatus(status.get(0));
            }
            if (null == getTicket().getAuthor()) {
                getTicket().setAuthor(user);
            }
            if (getTicket().getId() != 0) {
                ticketDao.merge(ticket);
            } else {
                ticketDao.save(ticket);
            }
        }
        return request.isXHR() ? zone.getBody() : null;
    }

    public User getUser() {
        return user;
    }

    public Ticket getTicket() {
        if (null == ticket) {
            ticket = null != ticketParam ? ticketParam : new Ticket();
        }
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Cached
    public SelectModel getStatusModel() {
        List<TicketStatus> list = ticketStatusDao.findByProject(ticket.getFeature().getProject());
        return selectModelFactory.create(list, "name");
    }

    @Cached
    public SelectModel getTypeModel() {
        List<TicketType> list = ticketTypeDao.findByProject(ticket.getFeature().getProject());
        return selectModelFactory.create(list, "name");
    }
}
