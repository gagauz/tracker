package com.gagauz.tracker.beans.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;

@Service
public class FeatureVersionDao extends AbstractDao<Integer, FeatureVersion> {

    @SuppressWarnings("unchecked")
    public List<FeatureVersion> findByVersion(Version version) {
        return getSession().createQuery("from FeatureVersion where version=:version").setEntity("version", version).list();
    }

    @SuppressWarnings("unchecked")
    public List<FeatureVersion> findByFeature(Feature feature) {
        return getSession().createQuery("from FeatureVersion where feature=:feature").setEntity("feature", feature).list();
    }

    public List<FeatureVersion> findByProject(Project project) {
        return getSession().createQuery("from FeatureVersion where feature.project=:project").setEntity("project", project).list();
    }

    public FeatureVersion findByFeatureAndVersion(int featureId, int versionId) {
        return (FeatureVersion) getSession().createQuery("from FeatureVersion where feature.id=:featureId and version.id=:versionId")
                .setInteger("featureId", featureId)
                .setInteger("versionId", versionId).uniqueResult();
    }

}
