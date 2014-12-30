package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Feature;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class FeatureCard {
    @Parameter
    @Property
    private Feature feature;
}
