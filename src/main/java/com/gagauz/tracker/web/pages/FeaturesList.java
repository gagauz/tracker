package com.gagauz.tracker.web.pages;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.FeatureVersionService;
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
    private Version version;

    @Property
    private Entry<Feature, Map<Version, List<Ticket>>> entry;

    @Inject
    private FeatureDao featureDao;

    @Inject
    private FeatureVersionService featureVersionService;

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

    @Cached
    public Map<Feature, Map<Version, List<Ticket>>> getFeatureVersionMap() {
        return featureVersionService.getFeatureVersionMap(project, false);
    }
}
