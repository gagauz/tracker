package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Task;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ClientBehaviorSupport;

public class LazyLoadZone {
    @Parameter
    @Property(write = false)
    private String zoneId;

    @Parameter
    private Object context;

    @Component(parameters = {"id=prop:zoneId"})
    private Zone zone;

    @Environmental
    private ClientBehaviorSupport clientBehaviorSupport;

    @Inject
    private ComponentResources componentResources;

    void setupRender() {
        clientBehaviorSupport.linkZone(zoneId + "-trigger", zoneId, getEventLink());
    }

    public Link getEventLink() {
        return componentResources.createEventLink("TriggerEvent", context);
    }

    Object onTriggerEvent(Task task) {
        comments = taskCommentDao.findByTask(task);
        return zone.getBody();
    }
}
