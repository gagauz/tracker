package com.gagauz.tracker.web.mixins;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.Collections;
import java.util.List;

@Events(EventConstants.PROVIDE_COMPLETIONS)
@MixinAfter
public class CoolAutocompleter {

    private static final String EVENT_NAME = "autocomplete";

    /**
     * The field component to which this mixin is attached.
     */
    @InjectContainer
    private Field field;

    @Inject
    private ComponentResources resources;

    @Environmental
    private JavaScriptSupport jsSupport;

    @Inject
    private TypeCoercer coercer;

    /**
     * Overwrites the default minimum characters to trigger a server round trip (the default is 1).
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private int minChars = 1;

    /**
     * Maximum number of suggestions shown in the UI. It maps to Typeahead's "limit" option. Default value: 5.
     */
    @Parameter("5")
    private int maxSuggestions;

    @Parameter("literal:id")
    private String displayId;

    @Parameter("literal:name")
    private String displayName;

    /**
     * The context for the "providecompletions" event.
     * This list of values will be converted into strings and included in
     * the URI. The strings will be coerced back to whatever their values are and made available to event handler
     * methods. The first parameter of the context passed to "providecompletions" event handlers will
     * still be the partial string typed by the user, so the context passed through this parameter
     * will be added from the second position on.
     *
     * @since 5.4
     */
    @Parameter
    private Object[] context;

    void beginRender(MarkupWriter writer) {
        writer.attributes("style", "display:none");
    }

    @Import(stylesheet = "core/typeahead-bootstrap3.css")
    void afterRender(MarkupWriter writer) {
        Link link = resources.createEventLink(EVENT_NAME, context);

        JSONObject spec = new JSONObject("id2", field.getClientId(), "id", field.getClientId() + "_ac",
                "url", link.toString()).put("minChars", minChars).put("limit", maxSuggestions).put("displayId", displayId).put("displayName", displayName);

        jsSupport.require("coolautocompleter").with(spec);

        Element input = writer.getElement();

        writer.element("input", "type", input.getAttribute("type"), "id", field.getClientId() + "_ac", "class", input.getAttribute("class"), "name",
                field.getControlName() + "_ac", "autocomplete", "off");
        writer.end();
    }

    Object onAutocomplete(List<String> context, @RequestParameter("t:input") String input) {
        final Holder<List> matchesHolder = Holder.create();

        // Default it to an empty list.

        matchesHolder.put(Collections.emptyList());

        ComponentEventCallback callback = new ComponentEventCallback() {
            @Override
            public boolean handleResult(Object result) {
                List matches = coercer.coerce(result, List.class);
                matchesHolder.put(matches);
                return true;
            }
        };

        Object[] newContext;
        if (context.size() == 0) {
            newContext = new Object[] {input};
        } else {
            newContext = new Object[context.size() + 1];
            newContext[0] = input;
            for (int i = 1; i < newContext.length; i++) {
                newContext[i] = context.get(i - 1);
            }
        }

        resources.triggerEvent(EventConstants.PROVIDE_COMPLETIONS, newContext, callback);

        JSONObject reply = new JSONObject();

        reply.put("matches", JSONArray.from(matchesHolder.get()));

        // A JSONObject response is always preferred, as that triggers the whole partial page render pipeline.
        return reply;
    }
}
