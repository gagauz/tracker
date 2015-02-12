package com.gagauz.tracker.web.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
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

    @Inject
    private ClientBehaviorSupport clientBehaviorSupport;

    Object beginRender(MarkupWriter writer) {
        Link link = getEventLink();
        Element element = writer.element("a", "href", link.toURI(), "style", "display:none");
        String linkId = getClientId() + "-trigger-link";
        element.forceAttributes("id", linkId);
        writer.write("sdfsdfas");
        writer.end();
        clientBehaviorSupport.linkZone(linkId, getClientId(), link);
        if (request.isXHR()) {
            javaScriptSupport.addScript(InitializationPriority.IMMEDIATE,
                    "$('%s').up('.t-zone').observe(Tapestry.ZONE_UPDATED_EVENT, function(){" +
                            "document.getElementById('%s').dispatchEvent(new MouseEvent('click', {view:window, bubbles:true, cancelable:true}));" +
                            "});", getClientId(), linkId);
            return getBody();
        } else {
            javaScriptSupport
                    .addScript(
                            InitializationPriority.LATE,
                            "$j(document).ready(function(){" +
                                    "document.getElementById('%s').dispatchEvent(new MouseEvent('click', {view:window, bubbles:true, cancelable:true}));" +
                                    "});",
                            linkId);
        }
        return emptyBlock;
    }

    boolean beforeRenderBody() {
        return false;
    }

    Block onEvent(EventContext context) {
        if (request.isXHR()) {
            return getBody();
        }
        return null;
    }

    public Link getEventLink() {
        return componentResources.createEventLink("event", context);
    }
}
