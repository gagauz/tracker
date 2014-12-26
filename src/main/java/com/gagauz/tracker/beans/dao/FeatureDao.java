package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Feature;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gagauz.tracker.db.utils.Param.param;

@Service
public class FeatureDao extends AbstractDao<Integer, Feature> {

    public List<Feature> findByProject(Project project) {
        return findByQuery("from Feature v where project=:project", param("project", project));
    }

}
