package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.RoleGroup;


@Service
public class RoleGroupDao extends AbstractDao<Integer, RoleGroup> {

    @SuppressWarnings("unchecked")
    public List<RoleGroup> findByProject(Project project) {
        return getSession().createQuery("from RoleGroup r where project=:project").setEntity("project", project).list();
    }

}
