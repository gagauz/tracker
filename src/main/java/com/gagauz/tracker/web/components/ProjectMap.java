package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.Collection;
import java.util.Map;

public class ProjectMap {

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property
    private Version version;

    @Property
    private Feature feature;

    @Property
    private SubTask subTask;

    private Map<Tuple, Task> map;

    @Cached
    public Map<Tuple, Task> getMap() {
        if (null == map) {
            map = CollectionFactory.newMap();

            for (Feature feature : project.getFeatures()) {
                for (Task task : feature.getTasks()) {
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

    public Task getTask() {
        return getMap().get(new Tuple(version, feature));
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
