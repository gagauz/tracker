package com.gagauz.tracker.web.security;

import com.gagauz.tracker.web.security.api.Credentials;

public class CredentialsImpl implements Credentials {

    private String username;
    private String password;
    private boolean remember;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRemember() {
        return remember;
    }

    public static CredentialsImpl rememberMe(String string, String string2) {
        CredentialsImpl credentials = new CredentialsImpl();
        credentials.username = string;
        credentials.password = string2;
        return credentials;
    }

    public static CredentialsImpl login(String username2, String password2, boolean remember) {
        CredentialsImpl credentials = new CredentialsImpl();
        credentials.username = username2;
        credentials.password = password2;
        credentials.remember = remember;
        return credentials;
    }

}
