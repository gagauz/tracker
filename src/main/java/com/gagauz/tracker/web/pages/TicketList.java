package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureVersionDao;

@Secured({ AccessRole.PROJECT_USER, AccessRole.PROJECT_ADMIN })
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
