package com.gagauz.tapestry.security;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectLoginHandler implements LoginHandler {

    private static final Logger log = LoggerFactory.getLogger(RedirectLoginHandler.class);

    @Inject
    private Request request;

    @Inject
    private Response response;

    @Inject
    @Value("${" + SecurityModule.SECURITY_REDIRECT_PARAMETER + "}")
    private String parameterName;

    @Override
    public void handle(SecurityUser oldUser, SecurityUser newUser, LoginResult result) {
        if (LoginResult.FAIL != result && null != request.getParameter(parameterName)) {
            log.info("Handle redirect after login");
            try {
                response.sendRedirect(request.getParameter(parameterName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
