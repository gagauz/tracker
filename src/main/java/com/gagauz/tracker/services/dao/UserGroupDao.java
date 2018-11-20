package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.UserGroup;

@Service
public class UserGroupDao extends AbstractDao<Integer, UserGroup> {

    public List<UserGroup> findByProject(Project project) {
        return getCriteriaFilter().eq("project", project).list();
    }

}
