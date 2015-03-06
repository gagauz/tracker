package com.gagauz.tracker.web.pages;

import org.gagauz.tapestry.security.LoginService;
import org.gagauz.tapestry.security.SecurityEncryptor;
import org.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.utils.StringUtils;
import com.gagauz.tracker.web.services.CredentialsUsernamePassword;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import javax.servlet.http.HttpServletResponse;

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
    private SecurityEncryptor encryptor;

    @Inject
    private HttpServletResponse response;

    Object onSubmitFromLoginForm() {
        if (!loginForm.getHasErrors()) {
            System.out.println("-------------------------------------------------");
            System.out.println(response.isCommitted());
            System.out.println("-------------------------------------------------");
            SecurityUser user = loginService.authenticate(new CredentialsUsernamePassword(username, password));
            if (null != user) {
                return Index.class;
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
