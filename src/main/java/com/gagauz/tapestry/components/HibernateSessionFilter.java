package com.gagauz.tapestry.components;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HibernateSessionFilter extends OpenSessionInViewFilter {
    protected Logger logger = LoggerFactory.getLogger("OpenSessionInViewFilter");

    private ThreadLocal<HttpServletRequest> req = new ThreadLocal<HttpServletRequest>();

    public HibernateSessionFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        req.set(request);
        if (isUseHibernateSessionInRequest(request)) {
            SessionFactory sessionFactory = lookupSessionFactory(request);
            boolean participate = false;

            if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
                // Do not modify the Session: just set the participate flag.
                participate = true;
                logger.warn("Use existing Session in OpenSessionInViewFilter for " + request.getRequestURI());
            } else {
                logger.debug("Opening Hibernate Session in OpenSessionInViewFilter for " + request.getRequestURI());
                Session session = openSession(sessionFactory);
                TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
            }

            try {
                filterChain.doFilter(request, response);
            } finally {
                if (!participate) {
                    SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                    logger.debug("Closing Hibernate Session in OpenSessionInViewFilter for " + request.getRequestURI());
                    closeSession(sessionHolder.getSession());
                } else {
                    logger.warn("Session used in OpenSessionInViewFilter for {} still open!", request.getRequestURI());
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
        Session session = super.openSession(sessionFactory);
        session.setFlushMode(FlushMode.AUTO);
        //session.setFlushMode(FlushMode.MANUAL);
        session.beginTransaction();
        logger.debug(String.format("begin transaction: %H in session: %H", session.getTransaction(), session));
        return session;
    }

    protected void closeSession(Session session) {
        if (session.isOpen()) {
            Transaction transaction = null;
            try {
                transaction = session.getTransaction();
                if (transaction.isActive()) {
                    logger.debug(String.format("try to commit transaction: %H in session: %H", transaction, session));
                    transaction.commit();
                } else {
                    logger.warn(String.format("session: %H has no active transaction, path = %s!", session, req.get().getServletPath()));
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
            logger.warn(String.format("session: %H is already closed!", session));
        }
    }

    protected boolean isUseHibernateSessionInRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.startsWith("/assets") || path.startsWith("/ajaxproxy"));
    }
}
