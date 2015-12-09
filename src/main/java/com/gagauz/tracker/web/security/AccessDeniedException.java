package com.gagauz.tracker.web.security;

import com.gagauz.tracker.web.security.api.AccessAttribute;

public class AccessDeniedException extends RuntimeException {

    private AccessAttribute accessAttribute;

    public AccessDeniedException(AccessAttribute accessAttribute) {
        this.accessAttribute = accessAttribute;
    }

    public AccessAttribute getNeedRoles() {
        return accessAttribute;
    }

}
