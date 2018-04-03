package org.gagauz.tracker.web.security;

import org.apache.tapestry5.security.api.Credentials;

public class CredentialsImpl implements Credentials {
    private String username;
    private String password;
    private boolean remember;

    public CredentialsImpl(String username2, String password2, boolean remember2) {
        this.username = username2;
        this.password = password2;
        this.remember = remember2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

}
