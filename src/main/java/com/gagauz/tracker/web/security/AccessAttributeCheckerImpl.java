package com.gagauz.tracker.web.security;

import com.gagauz.tracker.db.model.AccessRole;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.security.api.AccessAttribute;
import com.gagauz.tracker.web.security.api.AccessAttributeChecker;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;

public class AccessAttributeCheckerImpl implements AccessAttributeChecker {

    @Inject
    private ApplicationStateManager applicationStateManager;

    @Override
    public void check(AccessAttribute accessAttribute) throws AccessDeniedException {

        String[] needRoles = AccessRole.EMPTY_ROLES;

        if (accessAttribute instanceof AnnotationAccessAttribute) {
            needRoles = ((AnnotationAccessAttribute) accessAttribute).getRoles();
        }

        if (null != accessAttribute) {
            User manager = applicationStateManager.getIfExists(User.class);
            if (null != manager) {
                if (manager.checkRoles(needRoles)) {
                    return;
                }
            }
            throw new AccessDeniedException(accessAttribute);
        }
    }
}
