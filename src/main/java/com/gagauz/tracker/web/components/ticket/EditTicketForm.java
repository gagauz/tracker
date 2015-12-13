package com.gagauz.tracker.web.components.ticket;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.utils.Param;

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

    List<String> onProvideCompletions(String username) {
        List<String> res = new ArrayList<>();
        for (User u : userDao.findByQuery("from User u where username like :name or name like :name", Param.param("name", username + '%'))) {
            res.add(u.getUsername());
        }
        return res;
    }

    @Cached
    public FieldTranslator<User> getUsernameTranslator() {
        final ValueEncoder<User> encoder = valueEncoderSource.getValueEncoder(User.class);
        return new FieldTranslator<User>() {

            @Override
            public String toClient(User value) {
                return encoder.toClient(value);
            }

            @Override
            public void render(MarkupWriter writer) {

            }

            @Override
            public User parse(String input) throws ValidationException {
                return encoder.toValue(input);
            }

            @Override
            public Class<User> getType() {
                return User.class;
            }
        };
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
        final TicketStatus from = ticket.getStatus();
        list = F.flow(list).filter(new Predicate<TicketStatus>() {
            @Override
            public boolean accept(TicketStatus element) {
                return element.getAllowedFrom().contains(from);
            }
        }).toList();
        return selectModelFactory.create(list, "name");
    }
}
