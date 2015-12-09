package com.gagauz.tracker.web.services;

import org.gagauz.tapestry.security.api.Credentials;

public class CredentialsUsernamePassword implements Credentials {
    private final String username;
    private final String password;
    private final boolean remember;

    public CredentialsUsernamePassword(String username, String password, boolean remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRemember() {
        return remember;
    }
}
