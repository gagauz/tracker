package com.gagauz.tracker.web.services;

import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.utils.StringUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.gagauz.tapestry.security.LoginResult;
import org.gagauz.tapestry.security.SecurityEncryptor;
import org.gagauz.tapestry.security.api.LoginResultHandler;
import org.gagauz.tapestry.security.api.LogoutHandler;
import org.gagauz.tapestry.security.api.SecurityUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RememberMeHandler implements LoginResultHandler, LogoutHandler {

    public static final String REMEMBER_ME_COOKIE_NAME = "auth";
    public static final int REMEMBER_ME_COOKIE_AGE = 31536000;

    @Inject
    private HttpServletResponse response;

    @Inject
    private Request request;

    @Inject
    private SecurityEncryptor securityEncryptor;

    @Override
    public void handle(LoginResult result) {
        if (result.isSuccess() && !StringUtils.isEmpty(request.getParameter("remember"))) {
            if (result.getUser() != result.getOldUser()) {
                String hash = securityEncryptor.encrypt(((User) result.getUser()).getToken());
                Cookie cookie = new Cookie(REMEMBER_ME_COOKIE_NAME, hash);
                cookie.setMaxAge(REMEMBER_ME_COOKIE_AGE);
                cookie.setPath("/");
                response.addCookie(cookie);
                try {
                    response.flushBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
