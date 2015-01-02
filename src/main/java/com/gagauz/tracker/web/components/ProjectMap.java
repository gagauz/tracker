package com.gagauz.tracker.web.components;

import java.util.Collection;
import java.util.Map;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.db.model.Bug;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Version;

public class ProjectMap {

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property
    private Version version;

    @Property
    private Feature feature;

    private Task task;

    private Bug bug;

    private Map<Tuple, FeatureVersion> map;

    private int estimated = 0;
    private int progress = 0;

    @Inject
    private FeatureVersionDao featureVersionDao;

    void onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        featureVersionDao.save(featureVersion);
    }

    @Cached
    public Map<Tuple, FeatureVersion> getMap() {
        if (null == map) {
            map = CollectionFactory.newMap();

            for (Version version : project.getVersions()) {
                for (FeatureVersion task : version.getFeatureVersions()) {
                    map.put(new Tuple(task.getVersion(), feature), task);
                }
            }
        }
        return map;
    }

    @Cached
    public Collection<Version> getVersions() {
        return project.getVersions();
    }

    @Cached
    public Collection<Feature> getFeatures() {
        return project.getFeatures();
    }

    public FeatureVersion getFeatureVersion() {
        estimated = 0;
        progress = 0;
        return getMap().get(new Tuple(version, feature));
    }

    public String getProgress() {
        return estimated > 0 ? 100 * (progress / estimated) + "%" : "N/A";
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        estimated += task.getEstimated();
        progress += task.getProgress();
        this.task = task;
    }

    public Bug getBug() {

        return bug;
    }

    public void setBug(Bug bug) {
        estimated += bug.getEstimated();
        progress += bug.getProgress();
        this.bug = bug;
    }

    private class Tuple {
        private final Version version;
        private final Feature feature;
        private final int hash;

        Tuple(Version version, Feature feature) {
            this.feature = feature;
            this.version = version;
            this.hash = feature.hashCode() * version.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || (((Tuple) obj).hash == hash);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
