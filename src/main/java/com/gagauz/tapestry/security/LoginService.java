package com.gagauz.tapestry.security;

import com.gagauz.tapestry.security.api.Credentials;
import com.gagauz.tapestry.security.api.LoginHandler;
import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tapestry.security.api.SecurityUserProvider;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

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
            result = LoginResult.SUCCESS;
            for (LoginHandler handler : handlers) {
                handler.handle(oldUser, newUser, result);
            }
            return newUser;
        }

        return null;
    }
}
