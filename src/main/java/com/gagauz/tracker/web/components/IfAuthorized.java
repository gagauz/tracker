package com.gagauz.tracker.web.components;

import org.apache.tapestry5.corelib.base.AbstractConditional;

public class IfAuthorized extends AbstractConditional {

    @Override
    protected boolean test() {
        return true;
    }

}
