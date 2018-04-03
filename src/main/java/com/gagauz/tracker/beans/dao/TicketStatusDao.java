package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;
import com.xl0e.hibernate.dao.AbstractHibernateDao;

@Service
public class TicketStatusDao extends AbstractHibernateDao<Integer, TicketStatus> {

    public List<TicketStatus> findByProject(Project project) {
        return findByFilter(filter().eq("project", project));
    }

    public List<TicketStatus> findFirstByProject(Project project) {
        return findByFilter(filter().eq("project", project));
    }

    public List<TicketStatus> findCommon() {
        return findByFilter(filter().isNull("project"));
    }

}
