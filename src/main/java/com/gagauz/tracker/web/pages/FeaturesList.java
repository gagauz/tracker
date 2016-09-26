package com.gagauz.tracker.web.pages;

import java.util.List;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.gagauz.tracker.web.security.Secured;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;

@Secured
@Import(module = {"bootstrap/collapse"})
public class FeaturesList {

    @Property
    private Project project;

    @Property
    private Feature feature;

    @Property
    private FeatureVersion featureVersion;

    @Inject
    private FeatureDao featureDao;

    Object onActivate(EventContext ctx) {
        project = ctx.get(Project.class, 0);
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
