package com.gagauz.tapestry.security;

public interface LoginHandler {

    void handle(SecurityUser oldUser, SecurityUser newUser, LoginResult result);

}
