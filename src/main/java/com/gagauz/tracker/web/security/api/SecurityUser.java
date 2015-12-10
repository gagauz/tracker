package com.gagauz.tracker.web.security.api;

import com.gagauz.tracker.db.model.Roles;

public interface SecurityUser {
    boolean checkRoles(Roles[] needRoles);
}
