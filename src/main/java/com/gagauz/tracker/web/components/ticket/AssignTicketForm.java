package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.beans.dao.TicketCommentDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.WorkflowDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketComment;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.db.utils.Param;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

import java.util.ArrayList;
import java.util.List;

public class AssignTicketForm {
    @Component
    private Zone zone;

    @Component(parameters = {"context=ticket"})
    private Form form;

    @Parameter
    @Property
    private Ticket ticket;

    @Property
    private User owner;

    @Property
    private String comment;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private TicketCommentDao ticketCommentDao;

    @Inject
    private UserDao userDao;

    @Inject
    private WorkflowDao workflowDao;

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

    Object onSubmitFromForm(Ticket ticket) {
        this.ticket = ticket;
        if (!form.getHasErrors() && ticket.getOwner() != owner) {
            Workflow wlog = new Workflow();
            wlog.setFromOwner(ticket.getOwner());
            wlog.setToOwner(owner);

            ticket.setOwner(owner);
            ticketDao.merge(ticket);
            if (null != comment) {
                TicketComment comm = new TicketComment();
                comm.setAuthor(user);
                comm.setText(comment);
                comm.setTicket(ticket);
                ticketCommentDao.save(comm);
            }

            wlog.setTicket(ticket);
            workflowDao.save(wlog);
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
}
