package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * User: code8
 * Date: 04.02.13
 * Time: 19:21
 */
public class SecurityDispatcher implements Dispatcher {

    public static final String X_AJAX_AUTH = "X-Ajax-Auth";

    @Inject
    private Logger logger;

    @Override
    public boolean dispatch(Request request, Response response) throws IOException {
        return false;
    }

    protected boolean isAjaxAuth(Request request) {
        return request.isXHR() && Boolean.valueOf(request.getHeader(X_AJAX_AUTH));
    }
}
