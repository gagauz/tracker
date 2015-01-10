package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.TaskType;
import com.gagauz.tracker.db.model.Version;

@Secured
public class VersionInfo {

    @Property(write = false)
    private Version version;

    @Property
    private FeatureVersion featureVersion;

    @Property
    private Task task;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TaskDao taskDao;

    Object onActivate(Version version) {
        if (null == version) {
            return Index.class;
        }
        this.version = version;

        return null;
    }

    Object onPassivate() {
        return version;
    }

    @Cached
    public List<FeatureVersion> getFeatureVersions() {
        return featureVersionDao.findByVersion(version);
    }

    @Cached
    public List<Task> getAllTasks() {
        return taskDao.findByVersion(version);
    }

    @Cached
    public List<Task> getTasks() {
        return F.flow(getAllTasks()).filter(new Predicate<Task>() {
            @Override
            public boolean accept(Task element) {
                return element.getType() == TaskType.TASK;
            }
        }).toList();
    }

    @Cached
    public List<Task> getBugs() {
        return F.flow(getAllTasks()).filter(new Predicate<Task>() {
            @Override
            public boolean accept(Task element) {
                return element.getType() == TaskType.BUG;
            }
        }).toList();
    }
}
