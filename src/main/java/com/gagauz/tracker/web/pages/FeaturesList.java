package com.gagauz.tracker.web.pages;

import org.gagauz.tapestry.security.Secured;
import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

@Secured
public class FeaturesList {

    @Property
    private Project project;

    @Property
    private Feature feature;

    @Property
    private FeatureVersion featureVersion;

    @Inject
    private FeatureDao featureDao;

    Object onActivate(Project project) {
        if (null == project) {
            return Index.class;
        }
        this.project = project;
        return null;
    }

    Object onPassivate() {
        return project;
    }

    public List<Feature> getFeatures() {
        return featureDao.findByProject(project);
    }
}
