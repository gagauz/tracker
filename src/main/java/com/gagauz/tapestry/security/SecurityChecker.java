package com.gagauz.tapestry.security;

import javax.inject.Inject;

import com.gagauz.tracker.db.model.Role;

public class SecurityChecker {
    @Inject
    private SecurityUserCreator sessionUserCreator;

    public boolean isCurrentUserHasRoles(Role[] needRoles) {

        SecurityUser user = sessionUserCreator.getUserFromContext();
        if (null != user) {
            if (null == needRoles || 0 == needRoles.length) {
                return true;
            }
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
