package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.utils.StringUtils;
import com.gagauz.tracker.web.services.CredentialsUsernamePassword;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.gagauz.tapestry.security.LoginService;
import org.gagauz.tapestry.security.SecurityEncryptor;
import org.gagauz.tapestry.security.api.SecurityUser;

@Secure
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
    private LoginService loginService;

    @Inject
    private SecurityEncryptor encryptor;

    Object onSubmitFromLoginForm() {
        if (!loginForm.getHasErrors()) {
            SecurityUser user = loginService.authenticate(new CredentialsUsernamePassword(username, password, remember));
            if (null != user) {
                //return Index.class;
            }
        }
        return null;

    }

    public String getRoles() {
        if (null != request.getParameter("n")) {
            String[] strs = encryptor.decryptArray(request.getParameter("n"));
            return StringUtils.join(strs, ", ");
        }

        return null;

    }
}
