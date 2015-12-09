package com.gagauz.tracker.web.security;

import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RememberMeFilter extends AbstractCommonRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RememberMeFilter.class);

    static final String REMEMBER_ME_COOKIE_NAME = "auth_repetitor";
    static final int REMEMBER_ME_COOKIE_AGE = 31536000;

    @Inject
    private Cookies cookies;

    @Inject
    private CookieEncryptorDecryptor cookieDecryptor;

    @Inject
    private AuthenticationService authService;

    @Inject
    private ApplicationStateManager applicationStateManager;

    @Override
    public void handleInternal(AbstractCommonHandlerWrapper handlerWrapper) throws IOException {
        User user = applicationStateManager.getIfExists(User.class);
        if (null == user) {
            String cookieValue = cookies.readCookieValue(REMEMBER_ME_COOKIE_NAME);
            if (null != cookieValue) {
                log.info("Handle remember me cookie [{}]", cookieValue);
                try {
                    String[] credentials = cookieDecryptor.decryptArray(cookieValue);
                    user = authService.login(CredentialsImpl.rememberMe(credentials[0], credentials[1]));
                    if (null == user) {
                        throw new RuntimeException("remove cookie");
                    }
                } catch (Exception e) {
                    log.error("Failed to login with cookie. Remove it.", e);
                    cookies.removeCookieValue(REMEMBER_ME_COOKIE_NAME);
                }
            }
        }
        handlerWrapper.handle();
    }

}
