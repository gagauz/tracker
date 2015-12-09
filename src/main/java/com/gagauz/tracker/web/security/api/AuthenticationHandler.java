package com.gagauz.tracker.web.security.api;

public interface AuthenticationHandler<U extends SecurityUser, T extends Credentials> {
    void handleLogin(U newUser, T credentials);

    void handleLogout(U user);
}
