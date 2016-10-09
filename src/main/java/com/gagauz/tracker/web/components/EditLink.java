package com.gagauz.tracker.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class EditLink {
    @Parameter(autoconnect = true)
    @Property
    private Object row;
}
