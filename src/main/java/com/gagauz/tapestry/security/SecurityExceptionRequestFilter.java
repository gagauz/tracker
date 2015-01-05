package com.gagauz.tapestry.security;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.URLEncoder;

public class SecurityExceptionRequestFilter implements RequestFilter {

    @Inject
    @Value("${" + SecurityModule.SECURITY_LOGIN_FORM_URL + "}")
    private String loginFormUrl;

    @Inject
    @Value("${" + SecurityModule.SECURITY_REDIRECT_PARAMETER + "}")
    private String redirectParam;

    @Inject
    private URLEncoder urlEncoder;

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        try {
            System.out.println("--- Handle request " + request.getPath());
            return handler.service(request, response);
        } catch (IOException io) {
            // Pass it through.
            throw io;
        } catch (Throwable e) {
            System.out.println("--- Catch exception " + e);
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof SecurityException) {
                response.sendRedirect(loginFormUrl + '?' + redirectParam + '=' + urlEncoder.encode(request.getPath()));
                return true;
            }
            return false;
        }
    }
}
