package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.services.ApplicationStateManager;

public class SessionUserCreator {
    static final String USER_SESSION_PATH = "sso:" + SessionUser.class.getName();
    protected final ApplicationStateManager stateManager;

    public SessionUserCreator(ApplicationStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public SessionUser createUser(SessionUser user) {
        if (user != null) {
            stateManager.set(SessionUser.class, user);
            return user;
        }
        return null;
    }

    public SessionUser getUserFromContext() {
        return stateManager.getIfExists(SessionUser.class);
    }

}
