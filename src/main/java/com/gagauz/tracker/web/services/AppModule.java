package com.gagauz.tracker.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.hibernate.*;
import org.slf4j.Logger;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeFactoryDefaults(
                                                 MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(
                                                     MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
    }

    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    @ServiceId("TimingFilter")
    public RequestFilter buildTimingFilter(final Logger log) {
        return new RequestFilter() {
            public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
                long startTime = System.currentTimeMillis();

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    @ServiceId("HibernateFilter")
    public RequestFilter buildHibernateFilter(final Logger log, final SessionFactory sessionFactory) {
        return new RequestFilter() {
            public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
                boolean process = false;
                boolean participate = false;
                if (isUseHibernateSessionInRequest(request)) {
                    process = true;
                    log.info(String.format("Open hibernate session " + request.getPath()));
                    if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
                        // Do not modify the Session: just set the participate flag.
                        participate = true;
                        log.warn("Use existing Session in OpenSessionInViewFilter for " + request.getPath());
                    } else {
                        log.debug("Opening Hibernate Session in OpenSessionInViewFilter for " + request.getPath());
                        Session session = sessionFactory.openSession();
                        session.setFlushMode(FlushMode.MANUAL);
                        session.beginTransaction();
                        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
                    }
                }

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally {
                    if (process) {
                        if (!participate) {
                            SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                            log.debug("Closing Hibernate Session in OpenSessionInViewFilter for " + request.getPath());
                            Session session = sessionHolder.getSession();
                            if (null != session && session.isOpen()) {

                                Transaction transaction = null;
                                try {
                                    transaction = session.getTransaction();
                                    if (transaction.isActive()) {
                                        log.debug(String.format("try to commit transaction: %H in session: %H", transaction, session));
                                        transaction.commit();
                                    } else {
                                        log.warn(String.format("session: %H has no active transaction, path = %s!", session, request.getPath()));
                                    }
                                    session.close();
                                } catch (HibernateException e) {
                                    if (transaction != null) {
                                        transaction.rollback();
                                    }
                                    e.printStackTrace();
                                } catch (AssertionFailure af) {
                                    if (transaction != null) {
                                        transaction.rollback();
                                    }
                                    af.printStackTrace();
                                }
                            } else {
                                log.warn(String.format("session: %H is already closed!", session));
                            }

                        } else {
                            log.warn("Session used in OpenSessionInViewFilter for {} still open!", request.getPath());
                        }
                    }

                }
            }

            private boolean isUseHibernateSessionInRequest(Request request) {
                String path = request.getPath();
                return !(path.startsWith("/assets") || path.startsWith("/ajaxproxy"));
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @InjectService("TimingFilter") RequestFilter timing,
                                         @InjectService("HibernateFilter") RequestFilter hibernate)
    {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Hibernate", hibernate, "before:*");
        configuration.add("Timing", timing);
    }
}
