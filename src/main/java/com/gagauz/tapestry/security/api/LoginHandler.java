package com.gagauz.tapestry.security.api;

import com.gagauz.tapestry.security.LoginResult;

public interface LoginHandler {

    void handle(SecurityUser oldUser, SecurityUser newUser, LoginResult result);

}
