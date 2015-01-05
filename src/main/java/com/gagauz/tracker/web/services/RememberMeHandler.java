package com.gagauz.tracker.web.services;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import com.gagauz.tapestry.security.LoginService;
import com.gagauz.tapestry.security.LogoutHandler;
import com.gagauz.tapestry.security.SecurityEncryptor;
import com.gagauz.tapestry.security.SecurityUser;
import com.gagauz.tapestry.security.SecurityUserCreator;

public class RememberMeHandler implements LogoutHandler, ComponentEventRequestFilter, PageRenderRequestFilter {

    public static final String REMEMBER_ME_COOKIE_NAME = "auth";
    public static final int REMEMBER_ME_COOKIE_AGE = 31536000;

    @Inject
    private Cookies cookies;

    @Inject
    private SecurityEncryptor securityEncryptor;

    @Inject
    private SecurityUserCreator securityUserCreator;

    @Inject
    private LoginService loginService;

    @Override
    public void handle(SecurityUser user) {
        removeRememberMeCookie();
    }

    public void addRememberMeCookie(String username, String password) {
        String hash = securityEncryptor.encryptArray(username, password);
        cookies.writeCookieValue(REMEMBER_ME_COOKIE_NAME, hash, REMEMBER_ME_COOKIE_AGE);
    }

    public void removeRememberMeCookie() {
        cookies.removeCookieValue(REMEMBER_ME_COOKIE_NAME);
    }

    public void service(String page, Handler handler) throws IOException {
        if (null == securityUserCreator.getUserFromContext()) {
            String cookieValue = cookies.readCookieValue(REMEMBER_ME_COOKIE_NAME);
            if (null != cookieValue) {
                try {
                    String[] credentials = securityEncryptor.decryptArray(cookieValue);
                    loginService.authenticate(new CredentialsImpl(credentials[0], credentials[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                    removeRememberMeCookie();
                }
            }
        }
        handler.handle();
    }

    @Override
    public void handle(final ComponentEventRequestParameters parameters, final ComponentEventRequestHandler handler) throws IOException {
        service(parameters.getActivePageName(), new Handler() {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });

    }

    @Override
    public void handle(final PageRenderRequestParameters parameters, final PageRenderRequestHandler handler) throws IOException {
        service(parameters.getLogicalPageName(), new Handler() {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });
    }

    private interface Handler {
        void handle() throws IOException;
    }

}
