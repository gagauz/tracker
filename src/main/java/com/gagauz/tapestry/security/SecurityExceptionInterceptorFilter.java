package com.gagauz.tapestry.security;

import java.io.IOException;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityExceptionInterceptorFilter implements PageRenderRequestFilter, ComponentEventRequestFilter {

    protected static Logger logger = LoggerFactory.getLogger(SecurityExceptionInterceptorFilter.class);

    @Inject
    @Value("${" + SecurityModule.SECURITY_LOGIN_FORM_URL + "}")
    private String loginFormUrl;

    @Inject
    @Value("${" + SecurityModule.SECURITY_REDIRECT_PARAMETER + "}")
    private String redirectParam;

    @Inject
    private Response response;

    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;

    @Override
    public void handle(ComponentEventRequestParameters parameters, ComponentEventRequestHandler handler) throws IOException {
        try {
            handler.handle(parameters);
        } catch (IOException io) {
            // Pass it through.
            throw io;
        } catch (Throwable e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof SecurityException) {
                logger.info("Catch exception SecurityException");
                Link link = componentEventLinkEncoder.createComponentEventLink(parameters, false);
                response.sendRedirect(loginFormUrl + '?' + redirectParam + '=' + link.toRedirectURI());
                return;
            }
            throw new RuntimeException(e);
        }

    }

    @Override
    public void handle(PageRenderRequestParameters parameters, PageRenderRequestHandler handler) throws IOException {
        try {
            handler.handle(parameters);
        } catch (IOException io) {
            // Pass it through.
            throw io;
        } catch (Throwable e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof SecurityException) {
                logger.info("Catch exception SecurityException");
                Link link = componentEventLinkEncoder.createPageRenderLink(parameters);
                response.sendRedirect(loginFormUrl + '?' + redirectParam + '=' + link.toRedirectURI());
                return;
            }
            throw new RuntimeException(e);
        }

    }
}
