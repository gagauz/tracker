package com.gagauz.tracker.web.components;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.*;

@Import(stylesheet = "context:/static/css/project-map.css")
public class ProjectMap {

    @Component(parameters = {"id=prop:zoneId"})
    private Zone zone;

    @Component(parameters = {"id=literal:ticketZone"})
    private Zone ticketZone;

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property(write = false)
    private Version version;

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
    private Map<FeatureVersion, List<Ticket>> bugsMap;
    private Map<FeatureVersion, List<Ticket>> ticketsMap;

    private void initMap(List<Version> versions) {
        featureVersionMap = new HashMap<>();
        bugsMap = new HashMap<>();
        ticketsMap = new HashMap<>();
        for (Version version : versions) {
            Map<Feature, FeatureVersion> map = new HashMap<>();
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
    void onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        User user = new User();
        int id = securityUser.getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
        ajaxResponseRenderer.addRender(zone.getClientId(), zone.getBody());
    }

    @Ajax
    void onCreateTicket(FeatureVersion featureVersion) {
        newTicket = new Ticket();
        newTicket.setFeatureVersion(featureVersion);
        ajaxResponseRenderer
                .addRender(Layout.MODAL_BODY_ID, ticketZone.getBody())
                .addCallback(new JavaScriptCallback() {
                    @Override
                    public void run(JavaScriptSupport javascriptSupport) {
                        javascriptSupport.require("modal").invoke("showModal").with(Layout.MODAL_ID);
                    }
                });
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
