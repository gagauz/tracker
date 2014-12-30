package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

import java.io.IOException;

public class SecurityExceptionRequestFilter implements RequestFilter {

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        try {
            return handler.service(request, response);
        } catch (IOException io) {
            // Pass it through.
            throw io;
        } catch (Throwable e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof SecurityException) {
                response.sendRedirect("/login?page=" + request.getPath());
                return true;
            }
            return false;
        }
    }
}
