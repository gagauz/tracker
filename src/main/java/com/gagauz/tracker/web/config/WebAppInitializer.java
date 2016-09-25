package com.gagauz.tracker.web.config;

import org.gagauz.tapestry.web.config.AbstractWebApplicationInitializer;

import com.gagauz.tracker.web.services.AppModule;

public class WebAppInitializer extends AbstractWebApplicationInitializer {

    @Override
    protected Class<?> getAppModuleClass() {
        return AppModule.class;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { CommonConfiguration.class.getName() };
    }

    @Override
    protected boolean getUseExternalSpringContext() {
        return true;
    }

}
