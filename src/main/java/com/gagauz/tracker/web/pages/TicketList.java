package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Version;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class TicketList {

    @Property
    private FeatureVersion ticket;

    @Property(write = false)
    private Version version;

    @Inject
    private FeatureVersionDao ticketDao;

    Object onActivate(Version version) {
        if (null == version) {
            return Index.class;
        }

        this.version = version;

        return null;
    }

    public List<FeatureVersion> getTickets() {
        return ticketDao.findByVersion(version);
    }

}
