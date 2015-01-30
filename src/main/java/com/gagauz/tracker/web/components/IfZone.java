package com.gagauz.tracker.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;

public class IfZone extends Zone {

    private boolean display;

    @Parameter(name = "else", defaultPrefix = BindingConstants.LITERAL)
    private Block elseBlock;

    Object beginRender(MarkupWriter writer) {
        return display ? getBody() : elseBlock;
    }

    boolean beforeRenderBody() {
        return false;
    }
}
