package com.gagauz.tracker.beans.dao;

import static org.gagauz.hibernate.utils.EntityFilterBuilder.eq;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Project;

@Service
public class ProjectDao extends AbstractDao<Integer, Project> {

    public Project findByCode(String string) {
        return findOneByFilter(eq("code", string));
    }

}
