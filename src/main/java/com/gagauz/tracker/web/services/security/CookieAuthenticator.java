package com.gagauz.tracker.web.services.security;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

public class CookieAuthenticator extends SecurityAuthenticator implements PageRenderRequestFilter, ComponentEventRequestFilter {

    @Inject
    @Value("${" + SecurityModule.SECURITY_COOKIE_NAME + "}")
    private String cookieName;

    @Inject
    private Cookies cookies;

    public void service(String page, Handler handler) throws IOException {
        String cookieValue = cookies.readCookieValue(cookieName);
        if (null != cookieValue) {

            try {
                String[] credentials = decodeCookieValue(cookieValue);
                if (!authenticate(credentials[0], credentials[1])) {
                    throw new RuntimeException("Invalid cookie credentials " + credentials[0] + ", " + credentials[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                cookies.removeCookieValue(cookieName);
            }
        }
        handler.handle();
    }

    @Override
    public void handle(final ComponentEventRequestParameters parameters, final ComponentEventRequestHandler handler) throws IOException {
        service(parameters.getActivePageName(), new Handler() {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });

    }

    @Override
    public void handle(final PageRenderRequestParameters parameters, final PageRenderRequestHandler handler) throws IOException {
        service(parameters.getLogicalPageName(), new Handler() {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });
    }

    private interface Handler {
        void handle() throws IOException;
    }

}
