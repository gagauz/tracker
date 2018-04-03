package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketType;
import com.xl0e.hibernate.dao.AbstractHibernateDao;

@Service
public class TicketTypeDao extends AbstractHibernateDao<Integer, TicketType> {

    public List<TicketType> findByProject(Project project) {
        return findByFilter(filter().or().isNull("project").eq("project", project));
    }

    public List<TicketType> findCommon() {
        return findByFilter(filter().or().isNull("project"));
    }

}
