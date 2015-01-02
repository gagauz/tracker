package com.gagauz.tracker.web.services.hibernate;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.hibernate.AssertionFailure;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HibernateFilter implements ComponentEventRequestFilter, PageRenderRequestFilter {
    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private Logger log;

    private interface Handler {
        void handle() throws IOException;
    }

    public void service(String pageName, Handler handler) throws IOException {
        boolean process = false;
        boolean participate = false;
        process = true;
        log.info(String.format("Open hibernate session " + pageName));
        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            // Do not modify the Session: just set the participate flag.
            participate = true;
            log.warn("Use existing Session in OpenSessionInViewFilter for " + pageName);
        } else {
            log.debug("Opening Hibernate Session in OpenSessionInViewFilter for " + pageName);
            Session session = sessionFactory.openSession();
            session.setFlushMode(FlushMode.MANUAL);
            session.beginTransaction();
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        }

        try {
            // The responsibility of a filter is to invoke the corresponding method
            // in the handler. When you chain multiple filters together, each filter
            // received a handler that is a bridge to the next filter.

            handler.handle();
        } finally {
            if (process) {
                if (!participate) {
                    SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                    log.debug("Closing Hibernate Session in OpenSessionInViewFilter for " + pageName);
                    Session session = sessionHolder.getSession();
                    if (null != session && session.isOpen()) {

                        if (session.isDirty()) {
                            session.flush();
                        }

                        Transaction transaction = null;
                        try {
                            transaction = session.getTransaction();
                            if (transaction.isActive()) {
                                log.debug(String.format("try to commit transaction: %H in session: %H", transaction, session));
                                transaction.commit();
                            } else {
                                log.warn(String.format("session: %H has no active transaction, path = %s!", session, pageName));
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
                    log.warn("Session used in OpenSessionInViewFilter for {} still open!", pageName);
                }
            }

        }
    }

    @Override
    public void handle(final PageRenderRequestParameters parameters, final PageRenderRequestHandler handler) throws IOException {
        service(parameters.getLogicalPageName(), new Handler() {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });
    }

    @Override
    public void handle(final ComponentEventRequestParameters parameters, final ComponentEventRequestHandler handler) throws IOException {
        service(parameters.getActivePageName(), new Handler() {
            @Override
            public void handle() throws IOException {
                handler.handle(parameters);
            }
        });

    }

}
