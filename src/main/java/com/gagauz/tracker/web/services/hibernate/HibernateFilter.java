package com.gagauz.tracker.web.services.hibernate;

import com.gagauz.tracker.beans.common.HibernateSessionManager;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class HibernateFilter extends HibernateSessionManager implements RequestFilter {
    @Inject
    private SessionFactory sessionFactory;

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        boolean process = false;
        boolean participate = false;
        if (isUseHibernateSessionInRequest(request)) {
            process = true;
            participate = openSession();
        }

        try {
            return handler.service(request, response);
        } finally {
            if (process) {
                if (!participate) {
                    closeSession();
                } else {
                    logger.warn("Session used in OpenSessionInViewFilter for {} still open!", request.getPath());
                }
            }

        }
    }

    private boolean isUseHibernateSessionInRequest(Request request) {
        String path = request.getPath();
        return !(path.startsWith("/assets") || path.startsWith("/ajaxproxy"));
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
