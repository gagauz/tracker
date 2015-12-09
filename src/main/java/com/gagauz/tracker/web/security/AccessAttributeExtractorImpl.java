package com.gagauz.tracker.web.security;

import com.gagauz.tracker.web.security.api.AccessAttribute;
import com.gagauz.tracker.web.security.api.AccessAttributeExtractor;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;

public class AccessAttributeExtractorImpl implements AccessAttributeExtractor {

    @Override
    public AccessAttribute extract(PlasticClass plasticClass) {
        Secured annotation = plasticClass.getAnnotation(Secured.class);
        if (null != annotation) {
            return new AnnotationAccessAttribute(annotation.value());
        }
        return null;
    }

    @Override
    public AccessAttribute extract(PlasticClass plasticClass, PlasticMethod plasticMethod) {
        Secured annotation = plasticMethod.getAnnotation(Secured.class);
        if (null != annotation) {
            return new AnnotationAccessAttribute(annotation.value());
        }
        return null;
    }

}
