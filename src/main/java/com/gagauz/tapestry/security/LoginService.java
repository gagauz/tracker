package com.gagauz.tapestry.security;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

public class LoginService {

    @Inject
    private SecurityUserProvider sessionUserService;

    @Inject
    private SecurityUserCreator sessionUserCreator;

    @Inject
    private List<LoginHandler> handlers;

    public SecurityUser authenticate(Credentials credentials) {
        SecurityUser newUser = sessionUserService.loadByCredentials(credentials);

        LoginResult result = LoginResult.FAIL;

        if (null != newUser) {
            SecurityUser oldUser = sessionUserCreator.createUser(newUser);
            result = newUser.equals(oldUser) ? LoginResult.IGNORE : LoginResult.SUCCESS;
            for (LoginHandler handler : handlers) {
                handler.handle(oldUser, newUser, result);
            }
            return newUser;
        }

        return null;
    }
}
