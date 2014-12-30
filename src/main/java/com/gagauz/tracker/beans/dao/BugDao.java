package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.Bug;
import com.gagauz.tracker.db.model.FeatureVersion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugDao extends AbstractDao<Integer, Bug> {
    @SuppressWarnings("unchecked")
    public List<Bug> findByFeatureVersion(FeatureVersion featureVersion) {
        return getSession().createQuery("from Bug b where featureVersion=:featureVersion").setEntity("featureVersion", featureVersion).list();
    }

}
