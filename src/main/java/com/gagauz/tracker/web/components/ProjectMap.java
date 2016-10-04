package com.gagauz.tracker.web.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;

public class ProjectMap {

	@Component(parameters = { "id=prop:zoneId" })
	private Zone zone;

	@Component(parameters = { "id=literal:ticketZone" })
	private Zone ticketZone;

	@Component(parameters = { "id=literal:viewTicketZone" })
	private Zone viewTicketZone;

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
		this.featureVersionMap = new HashMap<>();
		this.bugsMap = new HashMap<>();
		this.ticketsMap = new HashMap<>();
		for (Version version : versions) {
			Map<Feature, FeatureVersion> map = new HashMap<>();
			for (Feature feature : getFeatures()) {
				map.put(feature, null);
			}
			this.featureVersionMap.put(version, map);
		}
	}

	@Cached
	public Collection<Version> getVersions() {
		List<Version> versions = this.releasedVersions
				? this.versionDao.findByProject(this.project, true)
				: this.versionDao.findByProject(this.project, false);
		if (null == this.featureVersionMap) {
			initMap(versions);

			for (FeatureVersion featureVersion : this.featureVersionDao.findByProject(this.project)) {
				Map<Feature, FeatureVersion> map = this.featureVersionMap.get(featureVersion.getVersion());
				if (null == map) {
					continue;
				}
				map.put(featureVersion.getFeature(), featureVersion);
				this.bugsMap.put(featureVersion, new ArrayList<Ticket>());
				this.ticketsMap.put(featureVersion, new ArrayList<Ticket>());
			}
			for (Ticket ticket : this.ticketDao.findByProject(this.project)) {
				addToMap(this.ticketsMap, ticket.getFeatureVersion(), ticket);
				// } else {
				// addToMap(bugsMap, ticket.getFeatureVersion(), ticket);
				// }
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
		return this.project.getFeatures();
	}

	public void setVersion(Version version0) {
		this.estimate = 0;
		this.progress = 0;
		this.version = version0;
		if (this.featureVersionMap != null) {
			Map<Feature, FeatureVersion> map = this.featureVersionMap.get(this.version);
			this.featureVersion = null != map ? map.get(this.feature) : null;
		}
	}

	@Ajax
	void onCreateFeatureVersion(Feature feature, Version version) {
		FeatureVersion featureVersion = new FeatureVersion();
		featureVersion.setFeature(feature);
		featureVersion.setVersion(version);
		User user = new User();
		int id = this.securityUser.getId();
		user.setId(id);
		featureVersion.setCreator(user);
		this.featureVersionDao.save(featureVersion);
		this.ajaxResponseRenderer.addRender(this.zone.getClientId(), this.zone.getBody());
	}

	@Ajax
	Object onCreateTicket(FeatureVersion featureVersion) {
		this.newTicket = new Ticket();
		this.newTicket.setFeatureVersion(featureVersion);
		this.newTicket.setAuthor(this.securityUser);

		return this.newTicket;
	}

	@Ajax
	Object onViewTicket(Ticket ticket) {
		this.newTicket = ticket;
		return this.newTicket;
	}

	public List<Ticket> getTickets() {
		return this.ticketsMap.get(this.featureVersion);
	}

	public List<Ticket> getBugs() {
		return this.bugsMap.get(this.featureVersion);
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	public void setTicket(Ticket ticket) {
		this.estimate += ticket.getEstimate();
		this.progress += ticket.getProgress();
		this.ticket = ticket;
	}

	public boolean isNotReleased() {
		return null == this.version || !this.version.isReleased();
	}

	public String getZoneId() {
		return "ProjectMapZone";
	}

	void onViewMode(String mode) {
		if ("a".equals(mode)) {
			this.releasedVersions = false;
			this.activeVersions = true;
		}
		if ("r".equals(mode)) {
			this.releasedVersions = true;
			this.activeVersions = false;
		}
	}

	public boolean isNoReleaseVersion() {
		return this.featureVersion == null;
	}
}
