package com.gagauz.tracker.web.security;

import com.gagauz.tracker.web.security.api.AccessDeniedHandler;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.io.IOException;
import java.util.List;

public class AccessDeniedExceptionInterceptorFilter extends AbstractCommonRequestFilter {

    @Inject
    private List<AccessDeniedHandler> handlers;

    @Override
    public void handleInternal(AbstractCommonHandlerWrapper handlerWrapper) throws IOException {
        try {
            handlerWrapper.handle();
        } catch (IOException io) {
            // Pass IOException through.
            throw io;
        } catch (Throwable e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof AccessDeniedException) {
                for (AccessDeniedHandler exceptionHandler : handlers) {
                    exceptionHandler.handleException(handlerWrapper, (AccessDeniedException) cause);
                }
                return;
            }
            // Pass non-security exception through.
            throw new RuntimeException(e);
        }
    }

}
