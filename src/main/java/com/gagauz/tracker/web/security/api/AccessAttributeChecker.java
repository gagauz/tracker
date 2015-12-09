package com.gagauz.tracker.web.security.api;

import com.gagauz.tracker.web.security.AccessDeniedException;

public interface AccessAttributeChecker {
    void check(AccessAttribute accessAttribute) throws AccessDeniedException;
}
