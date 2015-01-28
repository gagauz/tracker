package com.gagauz.tracker.db.model;

import java.io.Serializable;

public enum Role implements Serializable {
    VIEWER,
    ADMIN,
    PROJECT_ADMIN,
    PROJECT_USER,
    USER_STORY_CREATOR,
    TASK_CREATOR,
    TASK_ASSIGNER,
    BUG_CREATOR;

    public static final Role[] EMPTY_ARRAY = new Role[] {};
}
