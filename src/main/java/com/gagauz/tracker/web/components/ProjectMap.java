package com.gagauz.tracker.web.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.FeatureVersionDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.VersionDao;

public class ProjectMap {

    @Component(parameters = { "id=prop:zoneId" })
    @Property(write = false)
    private Zone zone;

    @Component(parameters = { "id=literal:ticketZone" })
    private Zone ticketZone;

    @Component(parameters = { "id=literal:viewTicketZone" })
    private Zone viewTicketZone;

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property(write = false)
    private Version version;

    @Persist(value = "flash")
    @Property
    private Version editVersion;

    @Property
    private Feature feature;

    @Property(write = false)
    private FeatureVersion featureVersion;

    private Ticket ticket;

    @Property
    private Ticket newTicket;

    @Property
    private int estimate;

    @Property
    private int progress;

    @Persist
    @Property
    private boolean activeVersions;

    @Persist
    @Property
    private boolean releasedVersions;

    @Inject
    private VersionDao versionDao;

    @Inject
    private FeatureDao featureDao;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TicketDao ticketDao;

    @SessionState
    private User securityUser;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    private Map<Version, Map<Feature, FeatureVersion>> featureVersionMap;
    private Map<FeatureVersion, List<Ticket>> ticketsMap;

    private void initMap(List<Version> versions) {
        featureVersionMap = new LinkedHashMap<>();
        ticketsMap = new HashMap<>();
        for (Version version : versions) {
            Map<Feature, FeatureVersion> map = new LinkedHashMap<>();
            for (Feature feature : getFeatures()) {
                map.put(feature, null);
            }
            featureVersionMap.put(version, map);
        }
    }

    @Cached
    public Collection<Version> getVersions() {
        List<Version> versions = releasedVersions
                ? versionDao.findByProject(project, true)
                : versionDao.findByProject(project, false);
        if (null == featureVersionMap) {
            initMap(versions);

            for (FeatureVersion featureVersion : featureVersionDao.findByProject(project)) {
                Map<Feature, FeatureVersion> map = featureVersionMap.get(featureVersion.getVersion());
                if (null == map) {
                    continue;
                }
                map.put(featureVersion.getFeature(), featureVersion);
                ticketsMap.put(featureVersion, new ArrayList<Ticket>());
            }
            for (Ticket ticket : ticketDao.findByProject(project)) {
                addToMap(ticketsMap, ticket.getFeatureVersion(), ticket);
            }
        }
        return versions;
    }

    private <K, V> void addToMap(Map<K, List<V>> map, K key, V value) {
        List<V> l = map.get(key);
        if (null == l) {
            l = new LinkedList<>();
            map.put(key, l);
        }
        l.add(value);
    }

    @Cached
    public Collection<Feature> getFeatures() {
        return featureDao.findByProject(project);
    }

    public void setVersion(Version version0) {
        estimate = 0;
        progress = 0;
        version = version0;
        if (featureVersionMap != null) {
            Map<Feature, FeatureVersion> map = featureVersionMap.get(version);
            featureVersion = null != map ? map.get(feature) : null;
        }
    }

    @Ajax
    Object onCreateTicket(FeatureVersion featureVersion) {
        newTicket = new Ticket();
        newTicket.setFeatureVersion(featureVersion);
        newTicket.setAuthor(securityUser);

        return newTicket;
    }

    @Ajax
    Object onEditVersion(Version version) {
        editVersion = version;
        return editVersion;
    }

    @Ajax
    Object onViewTicket(Ticket ticket) {
        newTicket = ticket;
        return newTicket;
    }

    public List<Ticket> getTickets() {
        List<Ticket> list = ticketsMap.get(featureVersion);
        return list;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        estimate += ticket.getEstimate();
        progress += ticket.getProgress();
        this.ticket = ticket;
    }

    public boolean isNotReleased() {
        return null == version || !version.isReleased();
    }

    public String getZoneId() {
        return "ProjectMapZone";
    }

    void onViewMode(String mode) {
        if ("a".equals(mode)) {
            releasedVersions = false;
            activeVersions = true;
        }
        if ("r".equals(mode)) {
            releasedVersions = true;
            activeVersions = false;
        }
    }

    public boolean isNoReleaseVersion() {
        return featureVersion == null;
    }
}
