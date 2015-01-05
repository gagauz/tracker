package com.gagauz.tapestry.security;

import com.gagauz.tracker.db.model.Role;

public interface SecurityUser {
    Role[] getRoles();
}
