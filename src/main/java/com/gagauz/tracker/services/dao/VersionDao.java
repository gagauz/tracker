package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;

@Service
public class VersionDao extends AbstractDao<Integer, Version> {

    @SuppressWarnings("unchecked")
    public List<Version> findByProject(Project project) {
        return getSession().createQuery("from Version v where project=:project").setEntity("project", project).list();
    }

    @SuppressWarnings("unchecked")
    public List<Version> findByProject(Project project, boolean released) {
        return getCriteriaFilter().eq("project", project).eq("released", released).list();
    }

    public Version findLast(Project project) {
        List<Version> list = getSession().createQuery("from Version v where project=:project order by name desc").setMaxResults(1)
                .setEntity("project", project)
                .list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<Version> findByFeature(Feature feature) {
        // TODO Auto-generated method stub
        return null;
    }

    public Version findByName(String name) {
        return getCriteriaFilter().eq("name", name).uniqueResult();
    }
}
