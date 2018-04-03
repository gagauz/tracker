package org.gagauz.tracker.web.security;

import org.apache.tapestry5.security.api.AccessAttributes;

import com.gagauz.tracker.db.model.AccessRole;

public class AccessAttributeImpl implements AccessAttributes {

    private final AccessRole[] roles;

    public AccessAttributeImpl(AccessRole[] roles) {
        this.roles = roles;
    }

    public AccessRole[] getRoles() {
        return roles;
    }
}
