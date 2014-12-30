package com.gagauz.tracker.web.services.security;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

public class SecurityModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(SecurityChecker.class).withId("SecurityChecker");
        binder.bind(SessionUserCreator.class).withId("SessionUserCreator");
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add("security.login-form.url", "/j_acegi_security_check");
    }

    @Contribute(ComponentClassTransformWorker2.class)
    public static void provideTransformWorkers(OrderedConfiguration<ComponentClassTransformWorker2> configuration, @Autobuild SecurityTransformer securityTransformer) {
        configuration.add("SecurityTransformer", securityTransformer, "after:*");
    }

    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration) {
        configuration.addInstance("ErrorFilter", SecurityExceptionRequestFilter.class, "after:ErrorFilter");
    }

    //
    //    @ServiceId("SecurityRegisterBuilder")
    //    public static PageLoader buildLoader(@Autobuild SecurityRegisterBuilder pageLoader,
    //                                         @ComponentTemplates InvalidationEventHub templatesHub,
    //                                         @ComponentMessages InvalidationEventHub messagesHub,
    //                                         @ComponentClasses InvalidationEventHub classesInvalidationEventHub) {
    //        classesInvalidationEventHub.addInvalidationListener(pageLoader);
    //        templatesHub.addInvalidationListener(pageLoader);
    //        messagesHub.addInvalidationListener(pageLoader);
    //        return pageLoader;
    //    }

}
