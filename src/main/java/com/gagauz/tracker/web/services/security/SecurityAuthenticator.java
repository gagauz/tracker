package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.utils.StringUtils;

public class SecurityAuthenticator {

    private static final String JOIN_STR = "_:&&:_";
    @Inject
    private SessionUserCreator sessionUserCreator;

    @Inject
    private SessionUserService sessionUserService;

    private SecurityEncryptor securityEncryptor;

    public boolean authenticate(final String usernameValue, final String passwordValue) {
        SessionUser user = sessionUserService.loadByCredentials(new Credentials() {

            @Override
            public String getUsername() {
                return usernameValue;
            }

            @Override
            public String getToken() {
                return null;
            }

            @Override
            public String getPassword() {
                return passwordValue;
            }
        });

        return handleAuthentication(user);
    }

    protected boolean handleAuthentication(SessionUser user) {
        if (null != user) {
            //handle auth
            sessionUserCreator.createUser(user);
            return true;
        }
        // handle error

        return false;
    }

    public boolean authenticate(final String tokenValue) {
        SessionUser user = sessionUserService.loadByCredentials(new Credentials() {

            @Override
            public String getUsername() {
                return null;
            }

            @Override
            public String getToken() {
                return tokenValue;
            }

            @Override
            public String getPassword() {
                return null;
            }
        });

        return handleAuthentication(user);
    }

    protected String encodeCookieValue(String... strings) {
        String joined = StringUtils.join(strings, JOIN_STR);
        return securityEncryptor.encrypt(joined);
    }

    protected String[] decodeCookieValue(String string) {
        string = securityEncryptor.decrypt(string);
        return StringUtils.split(string, JOIN_STR);
    }
}
