package com.gagauz.tracker.web.components.version;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureVersionDao;
import com.gagauz.tracker.services.dao.TicketDao;

public class FeatureVersionCard {

    @Parameter(autoconnect = true)
    @Property
    private Zone zone;

    @Component
    @Property
    private Zone ticketZone;

    @Parameter(allowNull = true)
    @Property
    private FeatureVersion featureVersion;

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
    private FeatureVersionDao featureVersionDao;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private TicketDao ticketDao;

    public boolean isAllowCreate() {
        return null == featureVersion && !version.isReleased();
    }

    public boolean isAllowRemove() {
        return null != featureVersion && ticketDao.findByFeatureAndVersion(feature, version).isEmpty();
    }

    @Ajax
    void onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        User user = new User();
        int id = securityUser.getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
        ajaxResponseRenderer.addRender(zone.getClientId(), zone.getBody());
    }

    @Ajax
    void onDeleteFeatureVersion(FeatureVersion featureVersion0) {
        featureVersionDao.delete(featureVersion0);
        ajaxResponseRenderer.addRender(zone.getClientId(), zone.getBody());
    }

    @Ajax
    Object onCreateTicket(FeatureVersion featureVersion0) {
        newTicket = new Ticket();
        newTicket.setFeatureVersion(featureVersion0);
        newTicket.setAuthor(securityUser);
        return newTicket;
    }

}
