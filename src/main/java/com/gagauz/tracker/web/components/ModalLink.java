package com.gagauz.tracker.web.components;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class ModalLink extends AbstractLink {
    @Parameter(defaultPrefix = BindingConstants.COMPONENT, allowNull = false, required = true)
    private Zone zone;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false, required = true)
    private String event;

    @Parameter
    private Object[] context;

    private String id;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private ComponentResources resources;

    @Ajax
    void onModal(EventContext context) {
        final String id = context.get(String.class, context.getCount() - 1);
        @SuppressWarnings("rawtypes")
        ComponentEventCallback handler = new ComponentEventCallback() {

            @Override
            public boolean handleResult(Object result) {
                ajaxResponseRenderer
                        .addRender(Layout.MODAL_BODY_ID, zone.getBody())
                        .addCallback(new JavaScriptCallback() {
                            @Override
                            public void run(JavaScriptSupport javascriptSupport) {
                                javascriptSupport.require("modal").invoke("showModal").with(Layout.MODAL_ID);
                            }
                        });
                return true;
            }
        };

        resources.getContainerResources().triggerContextEvent(event, context, handler);

    }

    void beginRender(MarkupWriter writer) {

        if (isDisabled())
            return;

        Link link = resources.createEventLink("modal", context);

        writeLink(writer, link);
        javaScriptSupport.require("t5/core/zone");
        writer.attributes("data-async-trigger", true);
    }

    void afterRender(MarkupWriter writer) {
        if (isDisabled())
            return;

        writer.end(); // <a>

        //        writer.element("div", "id", id + "_modal", "class", "modal fade", "role", "dialog", "tabindex", "-1");
        //        writer.element("div", "class", "modal-dialog modal-lg");
        //        writer.element("div", "class", "modal-content");
        //        writer.element("div", "class", "modal-header");
        //        writer.element("button", "type", "button", "class", "close", "data-dismiss", "modal", "aria-label", "Close");
        //        writer.element("span", "aria-hidden", "true");
        //        writer.writeRaw("×");
        //        writer.end();
        //        writer.end();
        //        writer.end();
        //        writer.element("div", "class", "modal-body");
        //        writer.element("div", "id", id + "_content");
        //        writer.end();
        //        writer.end();
        //        writer.end();
        //        writer.end();
        //        writer.end();
    }

    public static class MutableEventContext implements EventContext {
        private EventContext source;

        public MutableEventContext(EventContext source) {
            this.source = source;
        }

        @Override
        public int getCount() {
            return source.getCount() - 1;
        }

        @Override
        public <T> T get(Class<T> desiredType, int index) {
            return source.get(desiredType, index);
        }

        @Override
        public String[] toStrings() {
            String[] array = source.toStrings();
            return ArrayUtils.subarray(array, 0, array.length - 1);
        }
    }
}
