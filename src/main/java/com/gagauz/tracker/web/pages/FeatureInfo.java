package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.BugDao;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.Feature;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class FeatureInfo {

    @Property(write = false)
    private Feature feature;

    @Property
    private Task task;

    @Inject
    private BugDao bugDao;

    Object onActivate(Feature feature) {
        if (null == feature) {
            return ProjectList.class;
        }
        this.feature = feature;

        System.out.println(feature.getTasks().size());

        return null;
    }

    Object onPassivate() {
        return feature;
    }

}
