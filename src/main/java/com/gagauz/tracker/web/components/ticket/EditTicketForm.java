package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.beans.dao.Predicate;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.utils.Param;
import com.gagauz.tracker.utils.Filter;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SessionState;
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
    private SecurityUser user;

    public String getZoneId() {
        return zone.getClientId();
    }

    boolean setupRender() {
        if (null != ticketParam) {
            ticket = ticketParam;
        }
        return ticket != null;
    }

    Object onSubmitFromForm() {
        if (!form.getHasErrors()) {
            List<TicketStatus> status = statusDao.findByProject(getTicket().getFeatureVersion().getFeature().getProject());
            getTicket().setStatus(status)
            getTicket().setAuthor((User) user);
            ticketDao.save(getTicket());
        }
        return request.isXHR() ? zone.getBody() : null;
    }

    List<User> onProvideCompletions(String username) {
        return userDao.findByQuery("from User u where username like :name or name like :name", Param.param("name", username + '%'));
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
        return (User) user;
    }

    public Ticket getTicket() {
        if (null == ticket) {
            ticket = new Ticket();
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
        list = Filter.filter(list, new Predicate<TicketStatus>() {
            @Override
            public boolean apply(TicketStatus element) {
                return element.getAllowedFrom().contains(from);
            }
        });
        return selectModelFactory.create(list, "name");
    }
}
