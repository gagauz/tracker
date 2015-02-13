package com.gagauz.tracker.beans.common;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ThreadFactory;

public abstract class HibernateSessionManager implements ThreadFactory {

    protected Logger logger = LoggerFactory.getLogger(HibernateSessionManager.class);

    abstract protected SessionFactory getSessionFactory();

    @Override
    public Thread newThread(final Runnable r) {
        return new Thread() {
            @Override
            public void run() {
                boolean participate = false;
                openSession();
                try {
                    r.run();
                } finally {
                    if (!participate) {
                        closeSession();
                    } else {
                        logger.warn("Session used in OpenSessionInViewFilter for still open!");
                    }
                }
            }
        };
    }

    protected boolean openSession() throws HibernateException {
        SessionFactory sessionFactory = getSessionFactory();
        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            // Do not modify the Session: just set the participate flag.
            logger.warn("Use existing Hibernate Session");
            return true;
        }
        logger.debug("Opening Hibernate Session");
        Session session = openSession(sessionFactory);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        return false;
    }

    protected void closeSession() throws HibernateException {
        SessionFactory sessionFactory = getSessionFactory();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        logger.debug("Closing Hibernate Session");
        closeSession(sessionHolder.getSession());
    }

    protected Session openSession(SessionFactory sessionFactory) throws HibernateException {
        Session session = sessionFactory.openSession();
        session.setFlushMode(FlushMode.MANUAL);
        session.beginTransaction();
        logger.debug(String.format("begin transaction: %H in session: %H", session.getTransaction(), session));
        return session;
    }

    protected void closeSession(Session session) {
        if (session.isOpen()) {
            if (session.getFlushMode() == FlushMode.MANUAL) {
                session.flush();
            }
            Transaction transaction = null;
            try {
                transaction = session.getTransaction();
                if (transaction.isActive()) {
                    logger.debug(String.format("try to commit transaction: %H in session: %H", transaction, session));
                    transaction.commit();
                } else {
                    logger.warn(String.format("session: %H has no active transaction!", session));
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

}
