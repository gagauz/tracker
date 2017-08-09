package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;
import com.xl0e.hibernate.dao.AbstractDao;

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
}
