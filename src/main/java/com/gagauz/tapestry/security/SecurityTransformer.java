package com.gagauz.tapestry.security;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.runtime.ComponentEvent;
import org.apache.tapestry5.services.ComponentEventHandler;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gagauz.tracker.db.model.Role;

public class SecurityTransformer implements ComponentClassTransformWorker2 {
    protected static Logger logger = LoggerFactory.getLogger(SecurityTransformer.class);

    @Inject
    private SecurityChecker securityChecker;

    private MethodAdvice getSecurityAdvice(final PlasticMethod securedMethod, final Role[] needRoles) {
        return new MethodAdvice() {

            @Override
            public void advise(MethodInvocation invocation) {
                if (!securityChecker.isCurrentUserHasRoles(needRoles)) {
                    throw new SecurityException();
                }
            }
        };
    }

    @Override
    public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model) {
        final Secured annotation = plasticClass.getAnnotation(Secured.class);
        if (null != annotation) {
            support.addEventHandler(EventConstants.ACTIVATE, 0, "SecurityTransformer activate event handler", new ComponentEventHandler() {
                @Override
                public void handleEvent(Component instance, ComponentEvent event) {
                    System.out.println("-- Method invocation security advise " + event.toString() + " " + annotation.value());
                    if (!securityChecker.isCurrentUserHasRoles(annotation.value())) {
                        throw new SecurityException();
                    }
                }
            });
        }
        List<PlasticMethod> securedMethods = plasticClass.getMethodsWithAnnotation(Secured.class);
        for (PlasticMethod securedMethod : securedMethods) {
            final Secured methodAnnotation = securedMethod.getAnnotation(Secured.class);
            securedMethod.addAdvice(getSecurityAdvice(securedMethod, methodAnnotation.value()));
        }
    }
}
