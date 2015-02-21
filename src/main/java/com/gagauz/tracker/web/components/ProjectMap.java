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
import org.apache.tapestry5.services.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectMap {

    @Component(parameters = {"id=prop:zoneId", "show=", "update="})
    private Zone zone;

    @Component(parameters = {"id=literal:taskZone", "show=popup", "update=popup"})
    private Zone taskZone;

    @Parameter(allowNull = false, required = true, principal = true)
    private Project project;

    @Property
    private Version version;

    @Property(write = false)
    private Feature feature;

    @Property(write = false)
    private FeatureVersion featureVersion;

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

    @Inject
    private Request request;

    private Map<Version, Map<Feature, FeatureVersion>> featureVersionMap;
    private Map<FeatureVersion, List<Task>> bugsMap;
    private Map<FeatureVersion, List<Task>> tasksMap;

    private void initMap(List<Version> versions) {
        featureVersionMap = CollectionFactory.newMap();
        bugsMap = CollectionFactory.newMap();
        tasksMap = CollectionFactory.newMap();
        versions.add(null);
        for (Version version : versions) {
            Map<Feature, FeatureVersion> map = CollectionFactory.newMap();
            for (Feature feature : getFeatures()) {
                map.put(feature, null);
            }
            featureVersionMap.put(version, map);
        }
    }

    @Cached
    public Collection<Version> getVersions() {
        List<Version> versions = project.getVersions();
        if (null == featureVersionMap) {
            initMap(versions);

            for (FeatureVersion featureVersion : featureVersionDao.findByProject(project)) {
                featureVersionMap.get(featureVersion.getVersion()).put(featureVersion.getFeature(), featureVersion);
                bugsMap.put(featureVersion, new ArrayList<Task>());
                tasksMap.put(featureVersion, new ArrayList<Task>());
            }
            for (Task task : taskDao.findByProject(project)) {
                if (task.getType() == TaskType.TASK) {
                    tasksMap.get(task.getFeatureVersion()).add(task);
                } else {
                    bugsMap.get(task.getFeatureVersion()).add(task);
                }
            }
        }
        return versions;
    }

    @Cached
    public Collection<Feature> getFeatures() {
        return project.getFeatures();
    }

    public void setFeature(Feature featureLoop) {
        estimate = 0;
        progress = 0;
        feature = featureLoop;
        if (featureVersionMap != null) {
            Map<Feature, FeatureVersion> map = featureVersionMap.get(version);
            featureVersion = null != map ? map.get(feature) : null;
        }
    }

    Object onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        User user = new User();
        int id = ((User) securityUserCreator.getUserFromContext()).getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
        return request.isXHR() ? zone.getBody() : null;
    }

    Object onCreateTask(FeatureVersion featureVersion) {

        newTask = new Task();
        newTask.setFeatureVersion(featureVersion);
        //        newTask.setVersion(version);

        return taskZone.getBody();
    }

    public List<Task> getTasks() {
        return tasksMap.get(featureVersion);
    }

    public List<Task> getBugs() {
        return bugsMap.get(featureVersion);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        estimate += task.getEstimate();
        progress += task.getProgress();
        this.task = task;
    }

    public boolean isNotReleased() {
        return null == version || !version.isReleased();
    }

    public String getZoneId() {
        return "ProjectMapZone";
    }

}
