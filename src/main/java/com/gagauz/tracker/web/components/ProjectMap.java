package com.gagauz.tracker.web.components;

import com.gagauz.tapestry.security.SecurityUserCreator;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.db.model.*;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectMap {

    @Component(parameters = {"id=literal:taskZone", "show=popup", "update=popup"})
    private Zone taskZone;

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property
    private Version version;

    @Property
    private Feature feature;

    private Task task;

    @Property
    private Task newTask;

    @Property
    private int estimate;

    @Property
    private int progress;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TaskDao taskDao;

    @Inject
    private SecurityUserCreator securityUserCreator;

    private Map<Version, Map<Feature, FeatureVersion>> featureVersionMap;
    private Map<Integer, Map<Integer, List<Task>>> bugsMap;
    private Map<Integer, Map<Integer, List<Task>>> tasksMap;

    @Cached
    public Collection<Version> getVersions() {
        List<Version> versions = project.getVersions();
        if (null == featureVersionMap) {
            featureVersionMap = CollectionFactory.newMap();
            bugsMap = CollectionFactory.newMap();
            tasksMap = CollectionFactory.newMap();
            for (Version version : versions) {
                Map<Feature, FeatureVersion> map = CollectionFactory.newMap();
                Map<Integer, List<Task>> bugMap = CollectionFactory.newMap();
                Map<Integer, List<Task>> taskMap = CollectionFactory.newMap();
                for (Feature feature : getFeatures()) {
                    map.put(feature, null);
                    bugMap.put(feature.getId(), new ArrayList<Task>());
                    taskMap.put(feature.getId(), new ArrayList<Task>());
                }
                featureVersionMap.put(version, map);
                bugsMap.put(version.getId(), bugMap);
                tasksMap.put(version.getId(), taskMap);
            }

            for (FeatureVersion featureVersion : featureVersionDao.findByProject(project)) {
                featureVersionMap.get(featureVersion.getVersion()).put(featureVersion.getFeature(), featureVersion);
            }
            for (Task task : taskDao.findByProject(project)) {
                if (task.getType() == TaskType.TASK) {
                    tasksMap.get(task.getVersion().getId()).get(task.getFeature().getId()).add(task);
                } else {
                    bugsMap.get(task.getVersion().getId()).get(task.getFeature().getId()).add(task);
                }
            }
        }
        return versions;
    }

    @Cached
    public Collection<Feature> getFeatures() {
        return project.getFeatures();
    }

    public FeatureVersion getFeatureVersion() {
        estimate = 0;
        progress = 0;
        return featureVersionMap.get(version).get(feature);
    }

    void onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        User user = new User();
        int id = ((User) securityUserCreator.getUserFromContext()).getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
    }

    Object onCreateTask(Feature feature, Version version) {

        newTask = new Task();
        newTask.setFeature(feature);
        newTask.setVersion(version);

        return taskZone.getBody();
    }

    public List<Task> getTasks() {
        return tasksMap.get(version.getId()).get(feature.getId());
    }

    public List<Task> getBugs() {
        return bugsMap.get(version.getId()).get(feature.getId());
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        estimate += task.getEstimate();
        progress += task.getProgress();
        this.task = task;
    }

}
