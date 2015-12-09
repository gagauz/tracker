package com.gagauz.tracker.web.security.api;

public interface SecurityUser {
    boolean checkRoles(String[] needRoles);
}
