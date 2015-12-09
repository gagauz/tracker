package com.gagauz.tracker.web.security;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

// TODO: Auto-generated Javadoc
/**
 * The Class SecurityModule.
 */
public class SecurityModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(AccessDeniedExceptionInterceptorFilter.class);
        binder.bind(AuthenticationService.class);
    }

    public void contributeComponentEventRequestHandler(OrderedConfiguration<ComponentEventRequestFilter> configuration, @Local AccessDeniedExceptionInterceptorFilter filter) {
        configuration.add("AccessDeniedExceptionInterceptorFilterComponent", filter, "after:*");
    }

    public void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration, @Local AccessDeniedExceptionInterceptorFilter filter) {
        configuration.add("AccessDeniedExceptionInterceptorFilterPage", filter, "after:*");
    }

    @Contribute(ComponentClassTransformWorker2.class)
    public void contributeComponentClassTransformWorker2(OrderedConfiguration<ComponentClassTransformWorker2> configuration) {
        configuration.addInstance("SecurityTransformer", SecurityTransformer.class);
    }

}
