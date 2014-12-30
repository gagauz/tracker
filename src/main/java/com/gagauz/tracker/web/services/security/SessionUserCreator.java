package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.services.ApplicationStateManager;

public class SessionUserCreator {
    static final String USER_SESSION_PATH = "sso:" + SessionUser.class.getName();
    protected final ApplicationStateManager stateManager;
    protected final Class<SessionUser> clazz;

    public SessionUserCreator(ApplicationStateManager stateManager, Class<SessionUser> clazz) {
        this.stateManager = stateManager;
        this.clazz = clazz;
    }

    public SessionUser createUser(SessionUser user) {
        if (clazz.isAssignableFrom(user.getClass())) {
            stateManager.set(clazz, user);
            return user;
        }
        return null;
    }

    public SessionUser getUserFromContext() {
        return stateManager.getIfExists(clazz);
    }

}
