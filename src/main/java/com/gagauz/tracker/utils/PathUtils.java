package com.gagauz.tracker.utils;

import com.gagauz.tracker.db.model.Project;

public class PathUtils {
    public static String getAppBaseDir() {
        return System.getProperty("tracker.app-base-dir");
    }

    public static String getProjectBaseDir(Project project) {
        return getAppBaseDir() + '/' + project.getKey1();
    }
}
