package com.gagauz.tracker.services.dao;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.FeatureVersion.FeatureVersionId;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.utils.StringUtils;


@Service
public class FeatureVersionDao extends AbstractDao<FeatureVersionId, FeatureVersion> {

    public List<FeatureVersion> findByVersion(Version version) {
        return getCriteriaFilter().eq("version", version).list();
    }

    public List<FeatureVersion> findByFeature(Feature feature) {
        return getCriteriaFilter().eq("feature", feature).list();
    }

    public List<FeatureVersion> findByProject(Project project) {
        return getCriteriaFilter().eq("id.projectId", project.getId()).list();
    }

    public FeatureVersion findByFeatureAndVersion(int featureId, int versionId) {
        return getCriteriaFilter().eq("id.featureId", featureId).eq("id.versionId", versionId).uniqueResult();
    }

    @Override
    protected Function<String, FeatureVersionId> getIdDeserializer() {
        return string -> {
            if ("null".equalsIgnoreCase(string) || StringUtils.isEmpty(string)) {
                return null;
            }
            String[] ids = string.split("_");
            try {
                FeatureVersionId id = new FeatureVersionId();
                id.setFeatureId(Integer.parseInt(ids[0]));
                id.setVersionId(Integer.parseInt(ids[1]));
                return id;
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid FeatureVersionId string", e);
            }
        };
    }

    @Override
    public String idToString(FeatureVersionId id) {
        return null == id ? null : String.valueOf(id.getFeatureId()) + '_' + id.getVersionId();
    }
}
