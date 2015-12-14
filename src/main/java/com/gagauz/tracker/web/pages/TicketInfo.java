package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.db.model.Stage;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.web.components.DeferredZone;
import com.gagauz.tracker.web.security.Secured;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured
public class TicketInfo {

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

    public boolean isOwner() {
        return user.getId() == ticket.getOwner().getId();
    }

    @Cached
    public List<Stage> getStages() {
        return null;//ticket.getStages();
    }

}
