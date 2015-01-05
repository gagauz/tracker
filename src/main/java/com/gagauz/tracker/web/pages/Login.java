package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.gagauz.tapestry.security.LoginService;
import com.gagauz.tapestry.security.SecurityUser;
import com.gagauz.tracker.web.services.CredentialsImpl;
import com.gagauz.tracker.web.services.RememberMeHandler;

public class Login {
    @Property
    private String username;

    @Property
    private String password;

    @Property
    private boolean remember;

    @Inject
    private Request request;

    @Inject
    private LoginService loginService;

    @Inject
    private RememberMeHandler rememberMeHandler;

    Object onSuccessFromLoginForm() {
        SecurityUser user = loginService.authenticate(new CredentialsImpl(username, password));
        if (null != user) {
            rememberMeHandler.addRememberMeCookie(username, password);
        }

        return Index.class;
    }
}
