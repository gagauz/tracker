package com.gagauz.tapestry.security;

import com.gagauz.tapestry.security.api.LogoutHandler;

import com.gagauz.tapestry.security.api.SecurityUser;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutService {

    private static final Logger log = LoggerFactory.getLogger(LogoutService.class);

    @Inject
    private Request request;

    @Inject
    private SecurityUserCreator securityUserCreator;

    @Inject
    private List<LogoutHandler> handlers;

    public void logout() {

        SecurityUser user = securityUserCreator.getUserFromContext();

        for (LogoutHandler handler : handlers) {
            try {
                handler.handle(user);
            } catch (Exception e) {
                log.error("Failed to handle logout", e);
            }
        }

        Session session = request.getSession(false);

        if (null != session) {
            try {
                session.invalidate();
            } catch (Exception e) {
                log.error("Session invalidate error", e);
            }
        }
    }
}
