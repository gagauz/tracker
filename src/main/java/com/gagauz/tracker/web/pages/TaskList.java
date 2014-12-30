package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Version;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class TaskList {

    @Property
    private FeatureVersion task;

    @Property(write = false)
    private Version version;

    @Inject
    private FeatureVersionDao taskDao;

    Object onActivate(Version version) {
        if (null == version) {
            return Index.class;
        }

        this.version = version;

        return null;
    }

    public List<FeatureVersion> getTasks() {
        return taskDao.findByVersion(version);
    }

}
