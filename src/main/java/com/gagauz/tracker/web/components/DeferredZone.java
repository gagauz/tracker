package com.gagauz.tracker.web.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stack = "tracker-stack")
public class DeferredZone extends Zone {

    @Parameter(name = "empty", defaultPrefix = BindingConstants.LITERAL)
    private Block emptyBlock;

    @Parameter
    private Object context;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private Request request;

    Object beginRender() {
        if (request.isXHR()) {
            javaScriptSupport.addScript(InitializationPriority.IMMEDIATE,
                    "TapestryJQuery.updateZoneOnEvent($j('#%s').parents('.t-zone'), Tapestry.ZONE_UPDATED_EVENT, '%s', '%s');",
                    getClientId(),
                    getClientId(),
                    getEventLink().toURI());
        } else {
            javaScriptSupport.addScript(InitializationPriority.NORMAL, "TapestryJQuery.updateZoneOnEvent(document, 'ready', '%s', '%s');",
                    getClientId(),
                    getEventLink().toURI());
        }
        return emptyBlock;
    }

    boolean beforeRenderBody() {
        return false;
    }

    Block onEvent(EventContext context) {
        return getBody();
    }

    public Link getEventLink() {
        return componentResources.createEventLink("event", context);
    }
}
