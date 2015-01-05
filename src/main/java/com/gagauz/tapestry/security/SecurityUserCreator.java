package com.gagauz.tapestry.security;

import org.apache.tapestry5.services.ApplicationStateManager;

public class SecurityUserCreator {
    static final String USER_SESSION_PATH = "sso:" + SecurityUser.class.getName();
    protected final ApplicationStateManager stateManager;

    public SecurityUserCreator(ApplicationStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public SecurityUser createUser(SecurityUser newUser) {
        if (newUser != null) {
            SecurityUser oldUser = stateManager.getIfExists(SecurityUser.class);
            stateManager.set(SecurityUser.class, newUser);
            return oldUser;
        }
        return null;
    }

    public SecurityUser getUserFromContext() {
        return stateManager.getIfExists(SecurityUser.class);
    }

}
