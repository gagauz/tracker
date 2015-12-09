package com.gagauz.tracker.web.security;

import com.gagauz.tracker.web.security.api.AccessAttribute;

public class AnnotationAccessAttribute implements AccessAttribute {
    private final String[] roles;

    public AnnotationAccessAttribute(String... roles) {
        this.roles = roles;
    }

    public String[] getRoles() {
        return roles;
    }

}
