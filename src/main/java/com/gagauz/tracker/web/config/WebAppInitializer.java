package com.gagauz.tracker.web.config;

import org.apache.tapestry5.web.config.AbstractWebApplicationInitializer;

import com.gagauz.tracker.web.services.AppModule;

public class WebAppInitializer extends AbstractWebApplicationInitializer {

    @Override
    protected Class<?> getAppModuleClass() {
        return AppModule.class;
    }

    @Override
    protected String[] getSpringConfigLocations() {
        return new String[] { CommonConfiguration.class.getName() };
    }

    @Override
    protected boolean getUseExternalSpringContext() {
        return true;
    }

}
