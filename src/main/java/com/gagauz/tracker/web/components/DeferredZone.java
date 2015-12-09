package com.gagauz.tracker.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

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
            return getBody();
        }
        return emptyBlock;
    }

    void afterRender() {
        Link link = getEventLink();
        if (!request.isXHR()) {
            javaScriptSupport.addScript();
        }
    }

    boolean beforeRenderBody() {
        return false;
    }

    Block onLoad(EventContext context) {
        if (request.isXHR()) {
            return getBody();
        }
        return null;
    }

    public Link getEventLink() {
        return componentResources.createEventLink("load", context);
    }
}
