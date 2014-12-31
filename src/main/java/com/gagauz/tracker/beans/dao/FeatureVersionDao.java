package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Version;

@Service
public class FeatureVersionDao extends AbstractDao<FeatureVersion.FeatureVersionId, FeatureVersion> {

    @SuppressWarnings("unchecked")
    public List<FeatureVersion> findByVersion(Version version) {
        return getSession().createQuery("from Task t where version=:version").setEntity("version", version).list();
    }

    public List<FeatureVersion> findByFeature(Feature feature) {
        List<FeatureVersion> list = getSession().createQuery("from FeatureVersion fv where fv.feature_id=:feature")
                .setInteger("feature", feature.getId())
                .list();
        return list;
    }
}
