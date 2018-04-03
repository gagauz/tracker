package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.xl0e.hibernate.dao.AbstractHibernateDao;

@Service
public class FeatureDao extends AbstractHibernateDao<Integer, Feature> {

    public List<Feature> findByProject(Project project) {
        return findByFilter(filter().eq("project", project));
    }

}
