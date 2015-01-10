package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.annotations.Property;

import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Task;

public class FeatureVersionInfo {

    @Property(write = false)
    private FeatureVersion featureVersion;

    @Property
    private Task task;

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

}
