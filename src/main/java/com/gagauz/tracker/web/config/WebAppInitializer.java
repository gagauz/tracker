package com.gagauz.tracker.web.config;

import org.apache.tapestry5.web.config.AbstractWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.gagauz.tracker.config.DevCommonConfiguration;
import com.gagauz.tracker.utils.AppProperties;
import com.gagauz.tracker.web.services.AppModule;

public class WebAppInitializer extends AbstractWebApplicationInitializer {

    @Override
    protected Class<?> getAppModuleClass() {
        return AppModule.class;
    }

    @Override
    protected String[] getSpringConfigLocations() {
        return new String[] { DevCommonConfiguration.class.getName() };
    }

    @Override
    protected boolean getUseExternalSpringContext() {
        return true;
    }

    @Override
    protected void onSpringContext(AnnotationConfigWebApplicationContext context) {
        context.getEnvironment().setActiveProfiles(AppProperties.PROFILE.getString());
    }
}
