package com.gagauz.tapestry.security;

import com.gagauz.tapestry.security.api.SecurityExceptionHandler;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.io.IOException;
import java.util.List;

public class SecurityExceptionInterceptorFilter extends AbstractCommonRequestFilter {

    @Inject
    private List<SecurityExceptionHandler> handlers;

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
            if (cause instanceof SecurityException) {
                for (SecurityExceptionHandler exceptionHandler : handlers) {
                    exceptionHandler.handle(handlerWrapper, (SecurityException) cause);
                }
                return;
            }
            // Pass non-security exception through.
            throw new RuntimeException(e);
        }
    }

}
