package com.gagauz.tracker.db.model;

public enum AccessRole {
    ADMIN,
    ORDER_MANAGER,
    USER_MANAGER,
    BLOG_MANAGER,
    SEO_MANAGER;

    public static final String[] EMPTY_ROLES = new String[0];
}
