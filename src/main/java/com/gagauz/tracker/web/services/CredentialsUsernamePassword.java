package com.gagauz.tracker.web.services;

import org.gagauz.tapestry.security.api.Credentials;

public class CredentialsUsernamePassword implements Credentials {
    private final String username;
    private final String password;

    public CredentialsUsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
