package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Build;

@Service
public class BuildDao extends AbstractDao<Integer, Build> {

    @SuppressWarnings("unchecked")
    public List<Build> findByProject(Build project) {
        return createQuery("from Build v where project=:project").setEntity("project", project).list();
    }
}
