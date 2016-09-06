package com.gagauz.tracker.db.model;

public enum AccessRole {
    ANONYMOUS,
    ADMIN;

    public static final AccessRole[] EMPTY_ROLES = new AccessRole[0];
}
