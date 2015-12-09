package com.gagauz.tracker.web.components;

import com.gagauz.tracker.web.security.AccessDeniedException;
import com.gagauz.tracker.web.security.api.AccessAttribute;
import com.gagauz.tracker.web.security.api.AccessAttributeChecker;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;

public class IfAuthorized extends AbstractConditional {

    /** The roles. */
    @Parameter
    private AccessAttribute attribute;

    @Inject
    private AccessAttributeChecker accessAttributeChecker;

    @Override
    protected boolean test() {
        AccessAttribute accessAttribute = null == attribute
                ? AccessAttribute.EMPTY_ATTRIBUTE
                : attribute;
        try {
            accessAttributeChecker.check(accessAttribute);
        } catch (AccessDeniedException e) {
            return false;
        }
        return true;
    }
}
