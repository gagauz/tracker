package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionDao extends AbstractDao<Integer, Version> {

    @SuppressWarnings("unchecked")
    public List<Version> findByProject(Project project) {
        return getSession().createQuery("from Version v where project=:project").setEntity("project", project).list();
    }

    @SuppressWarnings("unchecked")
    public List<Version> findByProject(Project project, boolean released) {
        return getSession().createQuery("from Version v where project=:project and released=:released")
                .setEntity("project", project)
                .setBoolean("released", released)
                .list();
    }

    public Version findLast(Project project) {
        return (Version) getSession().createQuery("from Version v where project=:project order by name desc")
                .setEntity("project", project)
                .uniqueResult();
    }

}
