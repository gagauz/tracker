package com.gagauz.tracker.web.config;

import com.gagauz.tracker.web.services.AppModule;
import com.xl0e.web.config.AbstractWebApplicationInitializer;

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
