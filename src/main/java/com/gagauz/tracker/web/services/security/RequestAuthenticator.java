package com.gagauz.tracker.web.services.security;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

public class RequestAuthenticator extends SecurityAuthenticator implements RequestFilter {

    @Inject
    @Value("${" + SecurityModule.SECURITY_USERNAME_PARAMETER + "}")
    private String username;
    @Inject
    @Value("${" + SecurityModule.SECURITY_PASSWORD_PARAMETER + "}")
    private String password;
    @Inject
    @Value("${" + SecurityModule.SECURITY_REMEMBER_PARAMETER + "}")
    private String remember;
    @Inject
    @Value("${" + SecurityModule.SECURITY_COOKIE_NAME + "}")
    private String cookieName;
    @Inject
    @Value("${" + SecurityModule.SECURITY_COOKIE_NAME + "}")
    private String cookieAge;
    @Inject
    @Value("${" + SecurityModule.SECURITY_PASSWORD_PARAMETER + "}")
    private String token;
    @Inject
    @Value("${" + SecurityModule.SECURITY_REDIRECT_PARAMETER + "}")
    private String redirect;

    @Inject
    private Cookies cookies;

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        String redirectPage = request.getParameter(redirect);

        if (request.getMethod().equalsIgnoreCase("post")) {
            String usernameValue = request.getParameter(username);
            String passwordValue = request.getParameter(password);
            if (authenticate(usernameValue, passwordValue)) {
                if ("true".equalsIgnoreCase(request.getParameter(remember))) {
                    cookies.writeCookieValue(cookieName, encodeCookieValue(usernameValue, passwordValue), Integer.parseInt(cookieAge));
                }
                if (null != redirectPage) {
                    response.sendRedirect(redirectPage);
                    return true;
                }
            }
        } else if (request.getMethod().equalsIgnoreCase("get") && request.getParameter(token) != null) {
            if (authenticate(request.getParameter(token))) {
                if (null != redirectPage) {
                    response.sendRedirect(redirectPage);
                    return true;
                }
            }
        }
        return handler.service(request, response);
    }
}
