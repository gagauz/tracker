package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import org.gagauz.tracker.web.security.Secured;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured
public class VersionInfo {

    @Property(write = false)
    private Version version;

    @Property
    private FeatureVersion featureVersion;

    @Property
    private Ticket ticket;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TicketDao ticketDao;

    Object onActivate(Version version) {
        if (null == version) {
            return Index.class;
        }
        this.version = version;

        return null;
    }

    Object onPassivate() {
        return version;
    }

    @Cached
    public List<FeatureVersion> getFeatureVersions() {
        return featureVersionDao.findByVersion(version);
    }

    @Cached
    public List<Ticket> getAllTickets() {
        return ticketDao.findByVersion(version);
    }

    @Cached
    public List<Ticket> getTickets() {
        return getAllTickets();
    }

    @Cached
    public List<Ticket> getBugs() {
        return getAllTickets();
    }
}
