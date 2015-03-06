package com.gagauz.tracker.web.components;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;
import org.gagauz.tapestry.security.api.SecurityUser;

import java.util.*;

@Import(stylesheet = "context:/static/css/project-map.css")
public class ProjectMap {

    @Component(parameters = {"id=prop:zoneId", "show=", "update="})
    private Zone zone;

    @Component(parameters = {"id=literal:ticketZone", "show=popup", "update=popup"})
    private Zone ticketZone;

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property
    private Version version;

    @Property(write = false)
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

    @Inject
    private VersionDao versionDao;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TicketDao ticketDao;

    @SessionState
    private SecurityUser securityUser;

    @Inject
    private Request request;

    private Map<Version, Map<Feature, FeatureVersion>> featureVersionMap;
    private Map<FeatureVersion, List<Ticket>> bugsMap;
    private Map<FeatureVersion, List<Ticket>> ticketsMap;

    private void initMap(List<Version> versions) {
        featureVersionMap = CollectionFactory.newMap();
        bugsMap = CollectionFactory.newMap();
        ticketsMap = CollectionFactory.newMap();
        for (Version version : versions) {
            Map<Feature, FeatureVersion> map = CollectionFactory.newMap();
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
                bugsMap.put(featureVersion, new ArrayList<Ticket>());
                ticketsMap.put(featureVersion, new ArrayList<Ticket>());
            }
            for (Ticket ticket : ticketDao.findByProject(project)) {
                if (ticket.getType() == TicketType.TASK) {
                    addToMap(ticketsMap, ticket.getFeatureVersion(), ticket);
                } else {
                    addToMap(bugsMap, ticket.getFeatureVersion(), ticket);
                }
            }
        }
        return versions;
    }

    private <K, V> void addToMap(Map<K, List<V>> map, K key, V value) {
        List<V> l = map.get(key);
        if (null == l) {
            l = new LinkedList<V>();
            map.put(key, l);
        }
        l.add(value);
    }

    @Cached
    public Collection<Feature> getFeatures() {
        return project.getFeatures();
    }

    public void setFeature(Feature featureLoop) {
        estimate = 0;
        progress = 0;
        feature = featureLoop;
        if (featureVersionMap != null) {
            Map<Feature, FeatureVersion> map = featureVersionMap.get(version);
            featureVersion = null != map ? map.get(feature) : null;
        }
    }

    Object onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        User user = new User();
        int id = ((User) securityUser).getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
        return request.isXHR() ? zone.getBody() : null;
    }

    Object onCreateTicket(FeatureVersion featureVersion) {

        newTicket = new Ticket();
        newTicket.setFeatureVersion(featureVersion);

        return ticketZone.getBody();
    }

    public List<Ticket> getTickets() {
        return ticketsMap.get(featureVersion);
    }

    public List<Ticket> getBugs() {
        return bugsMap.get(featureVersion);
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

    @Persist
    @Property
    private boolean activeVersions;

    @Persist
    @Property
    private boolean releasedVersions;

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
