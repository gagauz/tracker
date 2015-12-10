package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.db.model.*;
import com.gagauz.tracker.utils.Comparators;
import com.gagauz.tracker.web.security.Secured;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.*;

@Secured
@Import(module = {"bootstrap/collapse"})
public class FeatureInfo {

    @Parameter
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

    @Cached
    public List<FeatureVersion> getFeatureVersions() {
        List<FeatureVersion> list = feature.getFeatureVersions();
        Collections.sort(list, Comparators.FEATURE_VERSION_BY_VERSION_COMPARATOR);
        return list;
    }

    @Cached
    public Map<Version, List<Ticket>> getMap() {
        Map<Version, List<Ticket>> map = new HashMap<>(feature.getFeatureVersions().size());
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

    public List<Ticket> getTasks() {
        return F.flow(getMap().get(featureVersion.getVersion())).filter(new Predicate<Ticket>() {
            @Override
            public boolean accept(Ticket element) {
                return element.getType() == TicketType.TASK;
            }
        }).toList();
    }

    public List<Ticket> getBugs() {
        return F.flow(getMap().get(featureVersion.getVersion())).filter(new Predicate<Ticket>() {
            @Override
            public boolean accept(Ticket element) {
                return element.getType() == TicketType.BUG;
            }
        }).toList();
    }
}
