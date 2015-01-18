package com.gagauz.tapestry.security;

import org.apache.tapestry5.services.*;

import java.io.IOException;

public abstract class AbstractCommonRequestFilter implements PageRenderRequestFilter, ComponentEventRequestFilter {

    public abstract void handleInternal(AbstractCommonHandlerWrapper handlerWrapper) throws IOException;

    @Override
    public void handle(final ComponentEventRequestParameters parameters, final ComponentEventRequestHandler handler) throws IOException {
        handleInternal(new AbstractCommonHandlerWrapper(parameters) {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });
    }

    @Override
    public void handle(final PageRenderRequestParameters parameters, final PageRenderRequestHandler handler) throws IOException {
        handleInternal(new AbstractCommonHandlerWrapper(parameters) {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });
    }
}
