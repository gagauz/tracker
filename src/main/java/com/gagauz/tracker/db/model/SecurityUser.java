package com.gagauz.tracker.db.model;

public interface SecurityUser {
    boolean checkRoles(String[] needRoles);
}
