package com.gagauz.tracker.web.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
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
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.VersionDao;
import com.xl0e.util.C;

@Import(stack = "app")
public class ProjectMap {

    @Component(parameters = { "id=prop:zoneId" })
    @Property(write = false)
    private Zone zone;

    @Component(parameters = { "id=literal:ticketZone" })
    @Property(write = false)
    private Zone ticketZone;

    @Component(parameters = { "id=literal:viewTicketZone" })
    private Zone viewTicketZone;

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property
    private Version version;

    @Property
    private Feature feature;

    @Persist(value = "flash")
    @Property
    private Version editVersion;

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
    private TicketDao ticketDao;

    @SessionState
    private User securityUser;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    private Map<Feature, Map<Version, List<Ticket>>> featureVersionMap;

    private void initMap() {
        featureVersionMap = new LinkedHashMap<>();
        List<Feature> features = featureDao.findByProject(project);
        List<Version> versions = versionDao.findByProject(project);
        for (Feature feature : features) {
            Map<Version, List<Ticket>> ticketsToVersion = new LinkedHashMap<>();
            featureVersionMap.put(feature, ticketsToVersion);
            versions.forEach(version -> ticketsToVersion.put(version, C.arrayList()));
            ticketsToVersion.put(null, C.arrayList());

            for (Ticket ticket : ticketDao.findByFeature(feature)) {
                ticketsToVersion.get(ticket.getVersion()).add(ticket);
            }
            featureVersionMap.put(feature, ticketsToVersion);
        }
    }

    @Cached
    public Collection<Version> getVersions() {
        if (null == featureVersionMap) {
            initMap();
        }
        return featureVersionMap.values().stream().findFirst().map(Map::keySet).orElse(Collections.emptySet());
    }

    public Collection<Version> getVersions(Feature feature) {
        return featureVersionMap.get(feature).keySet();
    }

    @Cached
    public Collection<Feature> getFeatures() {
        if (null == featureVersionMap) {
            initMap();
        }
        return featureVersionMap.keySet();
    }

    @Ajax
    Object onCreateTicket(Feature feature, Version version) {
        newTicket = new Ticket();
        newTicket.setFeature(feature);
        newTicket.setVersion(version);
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
        List<Ticket> list = featureVersionMap.get(feature).getOrDefault(version, Collections.emptyList());
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
        return false;
    }
}
