package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.annotations.Property;

import com.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;

@Secured
public class FeatureInfo {

    @Property(write = false)
    private Feature feature;

    @Property
    private FeatureVersion task;

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

}
