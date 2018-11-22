package com.gagauz.tracker.services.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.Version;

@Service
public class TicketDao extends AbstractDao<Integer, Ticket> {

	public List<Ticket> findByProject(Project project) {
		return getCriteriaFilter()
				.addAlias("feature", "feature")
				.eq("feature.project", project).list();
	}

	public List<Ticket> findByVersion(Version version) {
		return getCriteriaFilter().eq("version", version).list();
	}

	public List<Ticket> findByFeature(Feature feature) {
		return getCriteriaFilter().eq("feature", feature).list();
	}

	public void updateTicketProgessTime(Ticket ticket) {
		createSQLQuery("update ticket set progress=COALESCE((select sum(logTime) from work_log where ticket_id="
				+ ticket.getId()
				+ " group by ticket_id), 0) where id="
				+ ticket.getId()).executeUpdate();
	}

	public List<Ticket> findByFeatureAndVersion(Feature feature, Version version) {
		return getCriteriaFilter().eq("feature", feature)
				.eq("version", version).list();
	}

	@SuppressWarnings("unchecked")
	public Map<TicketStatus, Long> getVersionStatistics(Version version) {
		List<Object[]> result = getSession().createQuery("select t.status, count(*) "
				+ "from Ticket t "
				+ "where t.version=:version "
				+ "group by t.status.id")
				.setParameter("version", version)
				.getResultList();

		return result.stream().collect(Collectors.toMap(r -> (TicketStatus) r[0], r -> (Long) r[1]));
	}

	public long countTicketsByVersion(Version version) {
		return getCriteriaFilter().eq("version", version).count();
	}

	public List<Ticket> findByVersionAndStatuses(Version version, Set<TicketStatus> statuses) {
		return getCriteriaFilter()
				.eq("version", version)
				.in("status", statuses)
				.list();
	}

}
