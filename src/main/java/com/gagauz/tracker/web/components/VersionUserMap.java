package com.gagauz.tracker.web.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.gagauz.tapestry.security.SecurityUserCreator;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.web.services.ToolsService;

public class VersionUserMap {

    private static final Comparator<User> USER_NAME_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            if (null == o1) {
                return 1;
            }
            if (null == o2) {
                return -1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    };

    @Parameter(allowNull = false, required = true, principal = true)
    private Version version;

    @Component(parameters = {"id=literal:taskZone", "show=popup", "update=popup"})
    private Zone taskZone;

    @Property
    private Feature feature;

    private User user;

    private Task task;

    @Property
    private int estimated;

    @Property
    private int progress;

    @Property
    private Task viewTask;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TaskDao taskDao;

    @Inject
    private UserDao userDao;

    @Inject
    private SecurityUserCreator securityUserCreator;

    @Inject
    private ComponentResources resources;

    @Inject
    private ToolsService toolsService;

    private int endTime;
    private int rowEndTime;
    private int minTime;

    private Map<User, List<Task>> userTaskMap;

    @Cached
    public Collection<User> getUsers() {
        if (null == userTaskMap) {
            userTaskMap = CollectionFactory.newMap();
            minTime = Integer.MAX_VALUE;
            for (Task task : taskDao.findOpen(version)) {
                List<Task> tasks = userTaskMap.get(task.getOwner());
                if (null == tasks) {
                    tasks = new ArrayList<Task>();
                    userTaskMap.put(task.getOwner(), tasks);
                }
                tasks.add(task);
                minTime = Math.min(minTime, task.getEstimate() - task.getProgress());
            }
            minTime = 80 / (minTime + 1);
        }
        List<User> users = new ArrayList<User>(userTaskMap.keySet());
        Collections.sort(users, USER_NAME_COMPARATOR);

        return users;
    }

    public int getTasksWidth() {
        return rowEndTime;
    }

    public Collection<Task> getUserTasks() {
        return userTaskMap.get(user);
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

    public boolean isDraggable() {
        return task.getStatus() != TaskStatus.IN_PROGRESS;
    }

    public String getTasksTime() {
        return toolsService.getTime(rowEndTime);
    }

    public String getTaskTime() {
        return toolsService.getTime(task.getEstimate() - task.getProgress());
    }

    public String getEventUrl() {
        return resources.createEventLink("change").toRedirectURI();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        endTime = Math.max(rowEndTime, endTime);
        rowEndTime = 0;

        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        rowEndTime += task.getEstimate() - task.getProgress();
        this.task = task;
    }

    void onChange(@RequestParameter(value = "user") Integer userId, @RequestParameter(value = "task") Integer taskId) {
        User user = userDao.findById(userId);
        Task task = taskDao.findById(taskId);
        if (null != task) {
            task.setOwner(user);
        }
    }

    Object onViewTask(Task task) {
        viewTask = task;
        return taskZone.getBody();
    }

}
