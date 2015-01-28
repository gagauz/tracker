package com.gagauz.tapestry.binding;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

import java.util.Date;

public class DateBindingFactory implements BindingFactory {

    private static final String DELIMITER = ",";
    private final BindingSource bindingSource;
    private final TypeCoercer resolver;
    private final FastDateFormat format = FastDateFormat.getInstance("dd.MM.yyyy");

    public DateBindingFactory(BindingSource bindingSource, TypeCoercer resolver) {
        this.bindingSource = bindingSource;
        this.resolver = resolver;
    }

    @Override
    public Binding newBinding(final String description, final ComponentResources container, ComponentResources component, String expression, Location location) {
        final String[] parts = expression.split(DELIMITER, 2);

        return new AbstractContextBinding(bindingSource, resolver, description, container) {
            @Override
            public Object get() {
                Date conditionValue = getValue(parts[0], BindingConstants.PROP, Date.class);
                if (conditionValue != null) {
                    return format.format(conditionValue);
                }
                return "";
            }

        };
    }

}
