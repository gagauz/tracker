package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleGroupDao extends AbstractDao<Integer, RoleGroup> {

    @SuppressWarnings("unchecked")
    public List<RoleGroup> findByProject(Project project) {
        return getSession().createQuery("from RoleGroup r where project=:project").setEntity("project", project).list();
    }

}
