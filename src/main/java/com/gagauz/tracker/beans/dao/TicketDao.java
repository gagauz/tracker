package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.xl0e.hibernate.dao.AbstractDao;

@Service
public class TicketDao extends AbstractDao<Integer, Ticket> {

    public List<Ticket> findByProject(Project project) {
        return getCriteriaFilter().eq("featureVersion.id.projectId", project.getId()).list();
    }

    public List<Ticket> findByVersion(Version version) {
        return getCriteriaFilter().eq("featureVersion.id.versionId", version.getId()).list();
    }

    public List<Ticket> findByFeature(Feature feature) {
        return getCriteriaFilter().eq("featureVersion.id.featureId", feature.getId()).list();
    }

    public void updateTicketProgessTime(Ticket ticket) {
        createSQLQuery("update ticket set progress=COALESCE((select sum(logTime) from work_log where ticket_id="
                + ticket.getId() + " group by ticket_id), 0) where id=" + ticket.getId()).executeUpdate();
    }

}
