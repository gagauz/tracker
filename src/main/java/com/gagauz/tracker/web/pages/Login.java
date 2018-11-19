package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.security.AuthenticationService;
import org.apache.tapestry5.security.api.Credentials;
import org.apache.tapestry5.security.impl.UsernamePasswordCredentials;
import org.apache.tapestry5.services.Request;

import com.gagauz.tracker.db.model.User;

@Import(stylesheet = "context:/static/css/pages/Login.css")
public class Login {
    @Component
    private Form loginForm;

    @Property
    private String username;

    @Property
    private String password;

    @Property
    private boolean remember;

    @Inject
    private Request request;

    @Inject
    private AuthenticationService authenticationService;

    Object onSubmitFromLoginForm() {
        if (!loginForm.getHasErrors()) {
            Credentials credentials = new UsernamePasswordCredentials(username, password, true);
            User user = (User) authenticationService.login(credentials);
            if (null != user) {
                return Index.class;
            }
        }
        return null;

    }

    public String getRoles() {
        if (null != request.getParameter("n")) {
        }

        return null;

    }
}
