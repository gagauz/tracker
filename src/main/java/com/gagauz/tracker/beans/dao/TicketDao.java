package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketDao extends AbstractDao<Integer, Ticket> {

    public List<Ticket> findByProject(Project project) {
        return getSession().createQuery("from Ticket t "
                + "join fetch t.featureVersion tf "
                + "join fetch t.type tt "
                + "join fetch t.status ts "
                + "join fetch t.author ta "
                + "left join fetch t.owner to "
                + " where tf.id.feature.project=:project").setEntity("project", project)
                .list();
    }

    public List<Ticket> findByVersion(Version version) {
        return getSession().createQuery("from Ticket t where featureVersion.id.version=:version").setEntity("version", version).list();
    }

    public List<Ticket> findByFeature(Feature feature) {
        return getSession().createQuery("from Ticket t where featureVersion.id.feature=:feature").setEntity("feature", feature).list();
    }

    public void updateTicketProgessTime(Ticket ticket) {
        getSession().createSQLQuery("update ticket set progress=COALESCE((select sum(logTime) from work_log where ticket_id="
                + ticket.getId() + " group by ticket_id), 0) where id=" + ticket.getId()).executeUpdate();
    }

}
