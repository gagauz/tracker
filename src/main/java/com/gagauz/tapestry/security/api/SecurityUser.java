package com.gagauz.tapestry.security.api;

import com.gagauz.tracker.db.model.Role;

public interface SecurityUser {
    Role[] getRoles();
}
