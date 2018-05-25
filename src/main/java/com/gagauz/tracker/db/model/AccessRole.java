package com.gagauz.tracker.db.model;

public interface AccessRole {
    String VIEWER = "viewer";
    String ADMIN = "admin";
    String PROJECT_ADMIN = "project_admin";
    String PROJECT_USER = "project_user";
    String USER_STORY_CREATOR = "user_story_creator";
    String TASK_CREATOR = "task_creator";
    String TASK_ASSIGNER = "task_assigner";
    String BUG_CREATOR = "bug_creator";

    public static final String[] EMPTY = {};
}
