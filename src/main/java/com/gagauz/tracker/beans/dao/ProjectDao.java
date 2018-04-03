package com.gagauz.tracker.beans.dao;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.xl0e.hibernate.dao.AbstractHibernateDao;

@Service
public class ProjectDao extends AbstractHibernateDao<Integer, Project> {

    public Project findByCode(String string) {
        return findOneByFilter(filter().eq("code", string));
    }

}
