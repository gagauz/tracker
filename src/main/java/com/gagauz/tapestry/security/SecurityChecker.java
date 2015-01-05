package com.gagauz.tapestry.security;

import com.gagauz.tracker.db.model.Role;

import javax.inject.Inject;

public class SecurityChecker {
    @Inject
    private SecurityUserCreator sessionUserCreator;

    public boolean isCurrentUserHasRoles(Role[] needRoles) {

        if (null == needRoles || 0 == needRoles.length) {
            return true;
        }
        SecurityUser user = sessionUserCreator.getUserFromContext();
        if (null != user) {
            for (final Role userRole : user.getRoles()) {
                for (Role pageRole : needRoles) {
                    if (userRole == pageRole) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
