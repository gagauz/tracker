package com.gagauz.tapestry.security;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

public class SecurityModule {

    public static final String SECURITY_LOGIN_FORM_URL = "security.login-form.url";
    public static final String SECURITY_REDIRECT_PARAMETER = "security.parameter.redirect";

    public static void bind(ServiceBinder binder) {
        binder.bind(SecurityChecker.class).withId("SecurityChecker");
        binder.bind(SecurityUserCreator.class).withId("SessionUserCreator");
        binder.bind(SecurityExceptionInterceptorFilter.class).withId("SecurityExceptionRequestFilter2");
        binder.bind(LoginService.class).withId("LoginService");
        binder.bind(LogoutService.class).withId("LogoutService");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SECURITY_LOGIN_FORM_URL, "/login");
        configuration.add(SECURITY_REDIRECT_PARAMETER, "redirect");
    }

    @Contribute(ComponentClassTransformWorker2.class)
    public void contributeComponentClassTransformWorker2(OrderedConfiguration<ComponentClassTransformWorker2> configuration) {
        configuration.addInstance("SecurityTransformer", SecurityTransformer.class);
    }

    public void contributeComponentEventRequestHandler(OrderedConfiguration<ComponentEventRequestFilter> configuration,
                                                       @Local SecurityExceptionInterceptorFilter filter) {
        configuration.add("SecurityExceptionFilterComponent", filter, "after:*");
    }

    public void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration,
                                                   @Local SecurityExceptionInterceptorFilter filter) {
        configuration.add("SecurityExceptionFilterPage", filter, "after:*");
    }
}
