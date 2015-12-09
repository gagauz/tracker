package com.gagauz.tracker.web.security.api;

import org.apache.tapestry5.runtime.Component;

public class TextAccessAttribute implements AccessAttribute {
    private final String text;
    private final Component container;

    public TextAccessAttribute(Component container, String text) {
        this.container = container;
        this.text = text;
    }

    public Component getContainer() {
        return container;
    }

    public String getText() {
        return text;
    }
}
