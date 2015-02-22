package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.utils.Comparators;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.*;

@Secured
public class FeatureInfo {

    @Property(write = false)
    private Feature feature;

    @Property
    private FeatureVersion featureVersion;

    @Property
    private Task task;

    @Inject
    private TaskDao taskDao;

    Object onActivate(Feature feature) {
        if (null == feature) {
            return Index.class;
        }
        this.feature = feature;

        System.out.println(feature.getFeatureVersions().size());

        return null;
    }

    Object onPassivate() {
        return feature;
    }

    public List<FeatureVersion> getFeatureVersions() {
        List<FeatureVersion> list = feature.getFeatureVersions();
        Collections.sort(list, Comparators.FEATURE_VERSION_BY_VERSION_COMPARATOR);
        return list;
    }

    @Cached
    public Map<Version, List<Task>> getMap() {
        Map<Version, List<Task>> map = new HashMap<Version, List<Task>>(feature.getFeatureVersions().size());
        for (Task task : taskDao.findByFeature(feature)) {
            List<Task> tasks = map.get(task.getVersion());
            if (null == tasks) {
                tasks = new LinkedList<Task>();
                map.put(task.getVersion(), tasks);
            }
            tasks.add(task);
        }
        return map;
    }
}
