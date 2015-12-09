package com.gagauz.tracker.web.security.api;

import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;

public interface AccessAttributeExtractor {
    AccessAttribute extract(PlasticClass plasticClass);

    AccessAttribute extract(PlasticClass plasticClass, PlasticMethod plasticMethod);
}
