package com.gagauz.tracker.web.pages;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.utils.Comparators;

@Secured({ AccessRole.PROJECT_USER, AccessRole.PROJECT_ADMIN })
@Import(module = { "bootstrap/collapse" })
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
                tickets = new LinkedList<>();
                map.put(ticket.getVersion(), tickets);
            }
            tickets.add(ticket);
        }
        return map;
    }

    public List<Ticket> getTasks() {
        return getMap().get(featureVersion.getVersion());
    }

    public List<Ticket> getBugs() {
        return getMap().get(featureVersion.getVersion());
    }
}
