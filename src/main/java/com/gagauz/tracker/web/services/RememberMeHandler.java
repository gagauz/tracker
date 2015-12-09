package com.gagauz.tracker.web.services;

import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.gagauz.tapestry.security.LoginResult;
import org.gagauz.tapestry.security.SecurityEncryptor;
import org.gagauz.tapestry.security.api.Credentials;
import org.gagauz.tapestry.security.api.LoginResultHandler;
import org.gagauz.tapestry.security.api.LogoutHandler;
import org.gagauz.tapestry.security.api.SecurityUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class RememberMeHandler implements LoginResultHandler, LogoutHandler {

    public static final String REMEMBER_ME_COOKIE_NAME = "auth";
    public static final int REMEMBER_ME_COOKIE_AGE = 31536000;

    @Inject
    private HttpServletResponse response;

    @Inject
    private SecurityEncryptor securityEncryptor;

    @Override
    public void handle(LoginResult result, Credentials credentials) {
        if (result.isSuccess()
                && (credentials instanceof CredentialsUsernamePassword)
                && ((CredentialsUsernamePassword) credentials).isRemember()) {
            if (result.getUser() != result.getOldUser()) {
                String hash = securityEncryptor.encrypt(((User) result.getUser()).getToken());
                Cookie cookie = new Cookie(REMEMBER_ME_COOKIE_NAME, hash);
                cookie.setMaxAge(REMEMBER_ME_COOKIE_AGE);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    @Override
    public void handle(SecurityUser user) {
        //        cookies.removeCookieValue(REMEMBER_ME_COOKIE_NAME);
        Cookie cookie = new Cookie(REMEMBER_ME_COOKIE_NAME, null);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

}
