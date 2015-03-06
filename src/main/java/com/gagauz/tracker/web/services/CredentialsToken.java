package com.gagauz.tracker.web.services;

import org.gagauz.tapestry.security.api.Credentials;

public class CredentialsToken implements Credentials {
    private final String token;

    public CredentialsToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
