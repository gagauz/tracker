package com.gagauz.tracker.web.security;

import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.web.security.api.AuthenticationHandler;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RememberMeHandler implements AuthenticationHandler<User, CredentialsImpl> {

    private static final Logger log = LoggerFactory.getLogger(RememberMeHandler.class);

    static final String REMEMBER_ME_COOKIE_NAME = "auth_repetitor";
    static final int REMEMBER_ME_COOKIE_AGE = 31536000;

    @Inject
    private Cookies cookies;

    @Inject
    private CookieEncryptorDecryptor cookieEncryptor;

    @Override
    public void handleLogout(User user) {
        cookies.removeCookieValue(REMEMBER_ME_COOKIE_NAME);
    }

    @Override
    public void handleLogin(User user, CredentialsImpl loginDetails) {
        log.info("Handle login!");

        if (null != user
                && null != loginDetails
                && loginDetails.isRemember()) {
            String oldValue = cookies.readCookieValue(REMEMBER_ME_COOKIE_NAME);
            String hash = cookieEncryptor.encryptArray(loginDetails.getUsername(),
                    loginDetails.getPassword());
            if (null == oldValue || !oldValue.equals(hash)) {
                cookies.getBuilder(REMEMBER_ME_COOKIE_NAME, hash).setMaxAge(REMEMBER_ME_COOKIE_AGE).write();
            }
        }
    }

}
