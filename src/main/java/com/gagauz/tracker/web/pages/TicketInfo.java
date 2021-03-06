package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.services.dao.StageDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.web.components.DeferredZone;

@Secured
public class TicketInfo {

    @Component(parameters = { "id=viewTicketZone" })
    private Zone viewTicketZone;

    @Component
    @Property(write = false)
    private DeferredZone commentsZone;

    @Component
    @Property(write = false)
    private DeferredZone workflowZone;

    @Property(write = false)
    private Ticket ticket;

    @Property
    private Ticket child;

    @Property
    private Ticket viewTicket;

    @Persist
    @Property
    private boolean edit;

    @SessionState
    private User user;

    @Inject
    private TicketDao ticketDao;

    @Property
    private Stage stage;

    @Property
    private Workflow workflow;

    @Inject
    private StageDao stageDao;

    Object onActivate(Ticket ticket) {
        if (null == ticket) {
            return Index.class;
        }
        this.ticket = ticket;

        return null;
    }

    Object onPassivate() {
        return ticket;
    }

    @Ajax
    Object onViewTicket(Ticket ticket) {
        viewTicket = ticket;
        return viewTicket;
    }

    public boolean isOwner() {
        return user.getId() == ticket.getOwner().getId();
    }

    @Cached
    public List<Stage> getStages() {
        return null;// ticket.getStages();
    }

}
