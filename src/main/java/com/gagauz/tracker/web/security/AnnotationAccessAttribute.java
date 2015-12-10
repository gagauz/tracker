package com.gagauz.tracker.web.security;

import com.gagauz.tracker.db.model.Roles;
import com.gagauz.tracker.web.security.api.AccessAttribute;

public class AnnotationAccessAttribute implements AccessAttribute {
    private final Roles[] roles;

    public AnnotationAccessAttribute(Roles... roles) {
        this.roles = roles;
    }

    public Roles[] getRoles() {
        return roles;
    }

}
