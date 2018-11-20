package com.gagauz.tracker.services.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;

@Service
public class FeatureDao extends AbstractDao<Integer, Feature> {

    public List<Feature> findByProject(Project project) {
        return getCriteriaFilter().eq("project", project).orderDecs("updated").list();
    }

}
