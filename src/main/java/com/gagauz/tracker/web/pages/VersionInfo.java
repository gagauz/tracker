package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.Version;

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
        return F.flow(getAllTickets()).filter(new Predicate<Ticket>() {
            @Override
            public boolean accept(Ticket element) {
                return element.getType() == TicketType.TASK;
            }
        }).toList();
    }

    @Cached
    public List<Ticket> getBugs() {
        return F.flow(getAllTickets()).filter(new Predicate<Ticket>() {
            @Override
            public boolean accept(Ticket element) {
                return element.getType() == TicketType.BUG;
            }
        }).toList();
    }
}
