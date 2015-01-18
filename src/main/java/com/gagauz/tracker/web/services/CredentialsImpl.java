package com.gagauz.tracker.web.services;

import com.gagauz.tapestry.security.api.Credentials;

public class CredentialsImpl implements Credentials {
    private final String username;
    private final String password;

    public CredentialsImpl(String username, String password) {
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
