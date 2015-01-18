package com.gagauz.tapestry.security;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import java.io.IOException;

public abstract class AbstractCommonHandlerWrapper {
    private final ComponentEventRequestParameters componentEventRequestParameters;
    private final PageRenderRequestParameters pageRenderRequestParameters;

    public AbstractCommonHandlerWrapper(ComponentEventRequestParameters componentEventRequestParameters) {
        this.componentEventRequestParameters = componentEventRequestParameters;
        this.pageRenderRequestParameters = null;
    }

    public AbstractCommonHandlerWrapper(PageRenderRequestParameters pageRenderRequestParameters) {
        this.componentEventRequestParameters = null;
        this.pageRenderRequestParameters = pageRenderRequestParameters;
    }

    public abstract void handle() throws IOException;

    public ComponentEventRequestParameters getComponentEventRequestParameters() {
        return componentEventRequestParameters;
    }

    public PageRenderRequestParameters getPageRenderRequestParameters() {
        return pageRenderRequestParameters;
    }

}
