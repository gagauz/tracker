package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.utils.Comparators;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.*;

@Secured
public class FeatureInfo {

    @Property(write = false)
    private Feature feature;

    @Property
    private FeatureVersion featureVersion;

    @Property
    @Persist
    private boolean editMode;

    @Property
    private Ticket ticket;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private FeatureDao featureDao;

    Object onActivate(Feature feature) {
        if (null == feature) {
            return Index.class;
        }
        this.feature = feature;

        System.out.println(feature.getFeatureVersions().size());

        return null;
    }

    Object onPassivate() {
        return feature;
    }

    void onSuccessFromEditForm() {
        editMode = false;
        featureDao.save(feature);
    }

    void onEdit() {
        editMode = true;
    }

    public List<FeatureVersion> getFeatureVersions() {
        List<FeatureVersion> list = feature.getFeatureVersions();
        Collections.sort(list, Comparators.FEATURE_VERSION_BY_VERSION_COMPARATOR);
        return list;
    }

    @Cached
    public Map<Version, List<Ticket>> getMap() {
        Map<Version, List<Ticket>> map = new HashMap<Version, List<Ticket>>(feature.getFeatureVersions().size());
        for (Ticket ticket : ticketDao.findByFeature(feature)) {
            List<Ticket> tickets = map.get(ticket.getVersion());
            if (null == tickets) {
                tickets = new LinkedList<Ticket>();
                map.put(ticket.getVersion(), tickets);
            }
            tickets.add(ticket);
        }
        return map;
    }
}
