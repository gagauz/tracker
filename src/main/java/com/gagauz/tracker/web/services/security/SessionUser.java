package com.gagauz.tracker.web.services.security;

import com.gagauz.tracker.db.model.Role;

public interface SessionUser {
    Role[] getRoles();
}
