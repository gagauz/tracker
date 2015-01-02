package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

public class SecurityModule {

    public static final String SECURITY_USERNAME_PARAMETER = "security.parameter.username";
    public static final String SECURITY_PASSWORD_PARAMETER = "security.parameter.password";
    public static final String SECURITY_REMEMBER_PARAMETER = "security.parameter.remember";
    public static final String SECURITY_TOKEN_PARAMETER = "security.parameter.token";
    public static final String SECURITY_REDIRECT_PARAMETER = "security.parameter.redirect";
    public static final String SECURITY_LOGIN_FORM_URL = "security.login-form.url";
    public static final String SECURITY_COOKIE_NAME = "security.cookie-name";
    public static final String SECURITY_COOKIE_AGE = "security.cookie-age";

    public static void bind(ServiceBinder binder) {
        binder.bind(SecurityChecker.class).withId("SecurityChecker");
        binder.bind(SessionUserCreator.class).withId("SessionUserCreator");
        binder.bind(SecurityExceptionInterceptorFilter.class).withId("SecurityExceptionRequestFilter2");
        binder.bind(CookieAuthenticator.class).withId("CookieAuthenticator");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SECURITY_LOGIN_FORM_URL, "/login");
        configuration.add(SECURITY_USERNAME_PARAMETER, "username");
        configuration.add(SECURITY_PASSWORD_PARAMETER, "password");
        configuration.add(SECURITY_REMEMBER_PARAMETER, "remember");
        configuration.add(SECURITY_REDIRECT_PARAMETER, "redirect");
        configuration.add(SECURITY_TOKEN_PARAMETER, "token_id");
        configuration.add(SECURITY_COOKIE_NAME, "auth");
        configuration.add(SECURITY_COOKIE_AGE, "31536000");
    }

    @Contribute(ComponentClassTransformWorker2.class)
    public void contributeComponentClassTransformWorker2(OrderedConfiguration<ComponentClassTransformWorker2> configuration) {
        configuration.addInstance("SecurityTransformer", SecurityTransformer.class);
    }

    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration) {
        //        configuration.addInstance("SecurityExceptionFilter", SecurityExceptionRequestFilter.class, "after:*");
        configuration.addInstance("RequestAuthenticator", RequestAuthenticator.class, "after:AjaxFilter");

    }

    public void contributeComponentEventRequestHandler(OrderedConfiguration<ComponentEventRequestFilter> configuration,
                                                       @Local SecurityExceptionInterceptorFilter filter,
                                                       @Local CookieAuthenticator cookieAuthenticator) {
        configuration.add("SecurityExceptionFilterComponent", filter, "after:*");
        configuration.add("CookieAuthenticator", cookieAuthenticator, "before:*");
    }

    public void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration,
                                                   @Local SecurityExceptionInterceptorFilter filter,
                                                   @Local CookieAuthenticator cookieAuthenticator) {
        configuration.add("SecurityExceptionFilterPage", filter, "after:*");
        configuration.add("CookieAuthenticator", cookieAuthenticator, "before:*");
    }
}
