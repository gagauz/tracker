package com.gagauz.tracker.web.services.hibernate;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;

public class HibernateModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(HibernateFilter.class).withId("HibernateFilter");
    }

    @Contribute(ComponentEventRequestHandler.class)
    public void contributeComponentEventRequestHandler(OrderedConfiguration<ComponentEventRequestFilter> configuration, @Local HibernateFilter hibernateFilter) {
        configuration.add("HibernateComponentEventRequestHandler", hibernateFilter, "before:*");
    }

    @Contribute(PageRenderRequestHandler.class)
    public void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration, @Local HibernateFilter hibernateFilter) {
        configuration.add("HibernatePageRenderRequestHandler", hibernateFilter, "before:*");
    }
}
