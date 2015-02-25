package com.gagauz.tracker.web.pages;

import com.gagauz.tapestry.security.LoginService;
import com.gagauz.tapestry.security.SecurityEncryptor;
import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.utils.StringUtils;
import com.gagauz.tracker.web.services.CredentialsImpl;
import com.gagauz.tracker.web.services.RememberMeHandler;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

@Import(stylesheet = "context:/static/css/pages/Login.css")
public class Login {
    @Property
    private String username;

    @Property
    private String password;

    @Property
    private boolean remember;

    //    @Inject
    //    @Value("${" + RedirectLoginHandler.SECURITY_REDIRECT_PARAMETER + "}")
    //TODO
    @ActivationRequestParameter
    private Object redirect;

    @ActivationRequestParameter
    private Object n;

    @Inject
    private Request request;

    @Inject
    private LoginService loginService;

    @Inject
    private RememberMeHandler rememberMeHandler;

    @Inject
    private SecurityEncryptor encryptor;

    Object onSuccessFromLoginForm() {
        SecurityUser user = loginService.authenticate(new CredentialsImpl(username, password));
        if (null != user) {
            rememberMeHandler.addRememberMeCookie(username, password);
            return Index.class;
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
