package com.gagauz.tracker.web.services.hibernate;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;

public class HibernateModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(HibernateFilter.class).withId("HibernateFilter");
    }

    @Contribute(RequestHandler.class)
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration, @Local HibernateFilter hibernateFilter) {
        configuration.add("HibernateComponentEventRequestHandler", hibernateFilter, "before:*");
    }

}
