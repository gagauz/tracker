package com.gagauz.tracker.web.services;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.gagauz.tapestry.security.AbstractCommonHandlerWrapper;
import org.gagauz.tapestry.security.AbstractCommonRequestFilter;
import org.gagauz.tapestry.security.LoginService;
import org.gagauz.tapestry.security.SecurityEncryptor;
import org.gagauz.tapestry.security.api.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RememberMeRequestFilter extends AbstractCommonRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RememberMeRequestFilter.class);
    @Inject
    private LoginService loginService;

    @SessionState
    private SecurityUser securityUser;

    @Inject
    private SecurityEncryptor securityEncryptor;

    @Inject
    private Cookies cookies;

    @Override
    public void handleInternal(AbstractCommonHandlerWrapper handlerWrapper) throws IOException {
        if (null == securityUser) {
            String cookieValue = cookies.readCookieValue(RememberMeHandler.REMEMBER_ME_COOKIE_NAME);
            if (null != cookieValue) {
                log.info("Handle remember me cookie [{}]", cookieValue);
                try {
                    String credentials = securityEncryptor.decrypt(cookieValue);
                    loginService.authenticate(new CredentialsToken(credentials));
                } catch (Exception e) {
                    log.error("Failed to login with cookie. Remove it.", e);
                    cookies.removeCookieValue(RememberMeHandler.REMEMBER_ME_COOKIE_NAME);
                }
            }
        }
        handlerWrapper.handle();
    }
}
