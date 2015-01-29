package com.gagauz.tracker.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stack = "tracker-stack")
public class LazyZone extends Zone {

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String event;

    @Parameter(name = "empty", defaultPrefix = BindingConstants.LITERAL)
    private Block emptyBlock;

    @Parameter
    private Object context;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    /*
     *  Tapestry.Initializer.updateZoneOnEvent("click", spec.linkId,
            spec.zoneId, spec.url);
     */

    Object beginRender() {
        javaScriptSupport.addScript(InitializationPriority.NORMAL, "TapestryJQuery.updateZoneOnEvent(document, \"ready\",  \"%s\", \"%s\");",
                getClientId(),
                getEventLink().toURI());
        return emptyBlock;
    }

    boolean beforeRenderBody() {
        return false;
    }

    Block onEvent(EventContext context) {
        componentResources.getContainerResources().triggerContextEvent(event, context, new ComponentEventCallback<Object>() {
            @Override
            public boolean handleResult(Object result) {
                return true;
            }
        });

        return getBody();
    }

    public Link getEventLink() {
        return componentResources.createEventLink("event", context);
    }
}
