package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.BugDao;
import com.gagauz.tracker.db.model.Bug;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class FeatureVersionInfo {

    @Property(write = false)
    private FeatureVersion featureVersion;

    @Property
    private Task subTask;

    @Property
    private Bug bug;

    @Inject
    private BugDao bugDao;

    Object onActivate(FeatureVersion featureVersion) {
        if (null == featureVersion) {
            return Index.class;
        }
        this.featureVersion = featureVersion;
        return null;
    }

    Object onPassivate() {
        return featureVersion;
    }

    public List<Bug> getBugs() {
        return bugDao.findByFeatureVersion(featureVersion);
    }

}
