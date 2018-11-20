package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.services.dao.FeatureDao;

@Secured
@Import(module = { "bootstrap/collapse" })
public class FeaturesList {

    @PageActivationContext(index = 0)
    @Property
    private Project project;

    @Property
    private Feature feature;

    @Property
    private FeatureVersion featureVersion;

    @Inject
    private FeatureDao featureDao;

    Object onActivate(EventContext ctx) {
        if (null == project) {
            return Index.class;
        }
        return null;
    }

    Object onPassivate() {
        return project;
    }

    public List<Feature> getFeatures() {
        return featureDao.findByProject(project);
    }
}
