package com.gagauz.tracker.services;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.VersionDao;
import com.xl0e.util.C;

@Service
public class FeatureVersionService {

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private TicketDao ticketDao;

    public Map<Feature, Map<Version, List<Ticket>>> getFeatureVersionMap(Project project, boolean getTickets) {
        Map<Feature, Map<Version, List<Ticket>>> featureVersionMap = new LinkedHashMap<>();
        List<Feature> features = featureDao.findByProject(project);
        List<Version> versions = versionDao.findByProject(project);
        List<Ticket> tickets = getTickets ? ticketDao.findByProject(project) : Collections.emptyList();
        for (Feature feature : features) {
            Map<Version, List<Ticket>> ticketsToVersion = new LinkedHashMap<>();
            featureVersionMap.put(feature, ticketsToVersion);
            versions.forEach(version -> ticketsToVersion.put(version, C.arrayList()));
            ticketsToVersion.put(null, C.arrayList());
            featureVersionMap.put(feature, ticketsToVersion);
        }
        for (Ticket ticket : tickets) {
            featureVersionMap.get(ticket.getFeature()).get(ticket.getVersion()).add(ticket);
        }
        return featureVersionMap;
    }

    public Map<Version, List<Ticket>> getFeatureVersionMap(Feature feature) {
        Map<Version, List<Ticket>> featureVersionMap = new LinkedHashMap<>();
        List<Ticket> tickets = ticketDao.findByFeature(feature);
        featureVersionMap.put(null, C.arrayList());
        for (Ticket ticket : tickets) {
            featureVersionMap.computeIfAbsent(ticket.getVersion(), k -> C.arrayList()).add(ticket);
        }
        return featureVersionMap;
    }
}
