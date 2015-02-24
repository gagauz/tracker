package com.gagauz.tracker.web.components;

import com.gagauz.tapestry.security.SecurityUserCreator;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private SecurityUserCreator securityUserCreator;

    @Inject
    private Request request;

    private Map<Version, Map<Feature, FeatureVersion>> featureVersionMap;
    private Map<FeatureVersion, List<Ticket>> bugsMap;
    private Map<FeatureVersion, List<Ticket>> ticketsMap;

    private void initMap(List<Version> versions) {
        featureVersionMap = CollectionFactory.newMap();
        bugsMap = CollectionFactory.newMap();
        ticketsMap = CollectionFactory.newMap();
        versions.add(null);
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
        List<Version> versions = project.getVersions();
        if (null == featureVersionMap) {
            initMap(versions);

            for (FeatureVersion featureVersion : featureVersionDao.findByProject(project)) {
                featureVersionMap.get(featureVersion.getVersion()).put(featureVersion.getFeature(), featureVersion);
                bugsMap.put(featureVersion, new ArrayList<Ticket>());
                ticketsMap.put(featureVersion, new ArrayList<Ticket>());
            }
            for (Ticket ticket : ticketDao.findByProject(project)) {
                if (ticket.getType() == TicketType.TASK) {
                    ticketsMap.get(ticket.getFeatureVersion()).add(ticket);
                } else {
                    bugsMap.get(ticket.getFeatureVersion()).add(ticket);
                }
            }
        }
        return versions;
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
        int id = ((User) securityUserCreator.getUserFromContext()).getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
        return request.isXHR() ? zone.getBody() : null;
    }

    Object onCreateTicket(FeatureVersion featureVersion) {

        newTicket = new Ticket();
        newTicket.setFeatureVersion(featureVersion);
        //        newTicket.setVersion(version);

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

}
