package com.gagauz.tapestry.security.impl;

import com.gagauz.tapestry.security.AbstractCommonHandlerWrapper;
import com.gagauz.tapestry.security.LoginResult;
import com.gagauz.tapestry.security.SecurityEncryptor;
import com.gagauz.tapestry.security.SecurityException;
import com.gagauz.tapestry.security.api.LoginHandler;
import com.gagauz.tapestry.security.api.SecurityExceptionHandler;
import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.utils.AdapterUtils;
import com.gagauz.tracker.utils.Adapters;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

public class RedirectLoginHandler implements LoginHandler, SecurityExceptionHandler {

    public static final String SECURITY_REDIRECT_URL = "security-redirect-url";
    public static final String SECURITY_REDIRECT_PARAMETER = "security-redirect-parameter";

    @Inject
    @Value("${" + SECURITY_REDIRECT_URL + "}")
    private String loginFormUrl;

    @Inject
    @Value("${" + SECURITY_REDIRECT_PARAMETER + "}")
    private String redirectParam;

    @Inject
    private Request request;

    @Inject
    private HttpServletResponse response;

    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;

    @Inject
    private SecurityEncryptor encryptor;

    /**
     * On {@link SecurityException} redirects to login form page provided by loginFormUrl
     */
    @Override
    public void handle(AbstractCommonHandlerWrapper handlerWrapper, SecurityException cause) {
        try {
            Link link;
            if (handlerWrapper.getComponentEventRequestParameters() != null) {
                link = componentEventLinkEncoder.createComponentEventLink(handlerWrapper.getComponentEventRequestParameters(), false);
            } else {
                link = componentEventLinkEncoder.createPageRenderLink(handlerWrapper.getPageRenderRequestParameters());
            }

            response.sendRedirect(loginFormUrl + '?' + redirectParam + '=' + URLEncoder.encode(link.toRedirectURI()) + "&n="
                    + encryptor.encryptArray(AdapterUtils.transform(cause.getNeedRoles(), Adapters.ENUM_TO_STRING)));
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(SecurityUser oldUser, SecurityUser newUser, LoginResult result) {
        if (LoginResult.SUCCESS == result && null != request.getParameter(redirectParam)) {
            try {
                String s = request.getParameter(redirectParam);
                response.sendRedirect(request.getParameter(redirectParam));
                //response.flushBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
