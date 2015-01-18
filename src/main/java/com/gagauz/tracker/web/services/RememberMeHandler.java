package com.gagauz.tracker.web.services;

import com.gagauz.tapestry.security.*;
import com.gagauz.tapestry.security.api.LogoutHandler;
import com.gagauz.tapestry.security.api.SecurityUser;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RememberMeHandler extends AbstractCommonRequestFilter implements LogoutHandler {

    private static final Logger log = LoggerFactory.getLogger(RememberMeHandler.class);

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

    @Override
    public void handleInternal(AbstractCommonHandlerWrapper handlerWrapper) throws IOException {
        if (null == securityUserCreator.getUserFromContext()) {
            String cookieValue = cookies.readCookieValue(REMEMBER_ME_COOKIE_NAME);
            if (null != cookieValue) {
                log.info("Handle remember me cookie [{}]", cookieValue);
                try {
                    String[] credentials = securityEncryptor.decryptArray(cookieValue);
                    loginService.authenticate(new CredentialsImpl(credentials[0], credentials[1]));
                } catch (Exception e) {
                    log.error("Failed to login with cookie. Remove it.", e);
                    removeRememberMeCookie();
                }
            }
        }
        handlerWrapper.handle();
    }

}
