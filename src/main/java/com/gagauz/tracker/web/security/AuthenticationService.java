package com.gagauz.tracker.web.security;

import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.security.api.AuthenticationHandler;
import com.gagauz.tracker.web.security.api.UserProvider;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Inject
    private UserProvider<User, CredentialsImpl> userProvider;

    @Inject
    private ApplicationStateManager applicationStateManager;

    @Inject
    private List<AuthenticationHandler> handlers;

    @Inject
    private Request request;

    public User login(CredentialsImpl credentials) {
        User newUser = userProvider.findByCredentials(credentials);
        if (null != newUser) {
            Class clz = newUser.getClass();
            applicationStateManager.set(clz, newUser);
        }
        for (AuthenticationHandler handler : handlers) {
            handler.handleLogin(newUser, credentials);
        }

        return newUser;
    }

    public void logout() {

        User user = applicationStateManager.getIfExists(User.class);

        for (AuthenticationHandler handler : handlers) {
            handler.handleLogout(user);
        }

        applicationStateManager.set(User.class, null);

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
