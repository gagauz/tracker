package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;


@Service
public class FeatureDao extends AbstractDao<Integer, Feature> {

    public List<Feature> findByProject(Project project) {
        return findByFilter(filter().eq("project", project));
    }

}
