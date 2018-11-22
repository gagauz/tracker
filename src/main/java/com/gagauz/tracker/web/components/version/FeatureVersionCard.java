package com.gagauz.tracker.web.components.version;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.TicketDao;

public class FeatureVersionCard {

    @Parameter(autoconnect = true)
    @Property
    private Zone zone;

    @Parameter(autoconnect = true)
    @Property
    private Zone ticketZone;

    @Parameter
    @Property
    private Feature feature;

    @Parameter
    @Property
    private Version version;

    @Persist("flash")
    @Property
    private Ticket newTicket;

    @SessionState
    private User securityUser;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private TicketDao ticketDao;

    public boolean isCanCreateTicket() {
        return null == version || !version.isReleased();
    }

    @Ajax
    void onCreateFeatureVersion(Feature feature, Version version) {

    }

    @Ajax
    void onDeleteFeatureVersion(Feature feature, Version version) {

    }

    // @Ajax
    // Object onCreateTicket(FeatureVersion featureVersion0) {
    // newTicket = new Ticket();
    // newTicket.setFeatureVersion(featureVersion0);
    // newTicket.setAuthor(securityUser);
    // return newTicket;
    // }

}
