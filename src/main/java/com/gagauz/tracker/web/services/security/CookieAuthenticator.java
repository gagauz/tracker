package com.gagauz.tracker.web.services.security;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

public class CookieAuthenticator extends SecurityAuthenticator implements RequestFilter {

    @Inject
    @Value("${" + SecurityModule.SECURITY_COOKIE_NAME + "}")
    private String cookieName;

    @Inject
    private Cookies cookies;

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        String cookieValue = cookies.readCookieValue(cookieName);
        if (null != cookieValue) {
            String[] credentials = decodeCookieValue(cookieValue);
            try {
                if (!authenticate(credentials[0], credentials[1])) {
                    throw new RuntimeException("Invalid cookie credentials " + credentials[0] + ", " + credentials[1]);
                }
            } catch (Exception e) {
                cookies.removeCookieValue(cookieName);
            }
        }
        return handler.service(request, response);
    }
}
