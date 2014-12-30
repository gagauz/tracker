package com.gagauz.tracker.beans.dao;

import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Version;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureVersionDao extends AbstractDao<FeatureVersion.FeatureVersionId, FeatureVersion> {

    @SuppressWarnings("unchecked")
    public List<FeatureVersion> findByVersion(Version version) {
        return getSession().createQuery("from Task t where version=:version").setEntity("version", version).list();
    }

}
