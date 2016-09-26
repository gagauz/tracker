package com.gagauz.tracker.db.model;

public enum AccessRole {
    VIEWER,
    ADMIN,
    PROJECT_ADMIN,
    PROJECT_USER,
    USER_STORY_CREATOR,
    TASK_CREATOR,
    TASK_ASSIGNER,
    BUG_CREATOR;

    private static final AccessRole[] EMPTY = new AccessRole[0];

    public static AccessRole[] getEmpty() {
        return EMPTY;
    }
}
