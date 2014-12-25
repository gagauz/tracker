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

}
