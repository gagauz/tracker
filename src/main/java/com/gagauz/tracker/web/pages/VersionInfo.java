package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureVersionDao;
import com.gagauz.tracker.services.dao.TicketDao;

@Secured({ AccessRole.PROJECT_USER, AccessRole.PROJECT_ADMIN })
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
