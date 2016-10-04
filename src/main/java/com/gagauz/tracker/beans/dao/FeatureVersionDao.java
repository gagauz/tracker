package com.gagauz.tracker.beans.dao;

import java.util.List;
import java.util.function.Function;

import org.gagauz.utils.StringUtils;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.FeatureVersion.FeatureVersionId;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;

@Service
public class FeatureVersionDao extends AbstractDao<FeatureVersion.FeatureVersionId, FeatureVersion> {

	@SuppressWarnings("unchecked")
	public List<FeatureVersion> findByVersion(Version version) {
		return getSession().createQuery("from FeatureVersion where id.version=:version").setEntity("version", version).list();
	}

	@SuppressWarnings("unchecked")
	public List<FeatureVersion> findByFeature(Feature feature) {
		return getSession().createQuery("from FeatureVersion where id.feature=:feature").setEntity("feature", feature).list();
	}

	public List<FeatureVersion> findByProject(Project project) {
		return getSession().createQuery("from FeatureVersion where id.feature.project=:project").setEntity("project", project).list();
	}

	public FeatureVersion findByFeatureAndVersion(int featureId, int versionId) {
		return (FeatureVersion) getSession().createQuery("from FeatureVersion where id.feature.id=:featureId and id.version.id=:versionId")
				.setInteger("featureId", featureId)
				.setInteger("versionId", versionId).uniqueResult();
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
}
