package com.gagauz.tapestry.binding;

import com.gagauz.tracker.utils.StringUtils;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

/**
 * User: code8
 * Date: 12.06.13
 * Time: 18:15
 */
public class CondBindingFactory implements BindingFactory {
    private static final String DELIMETER = "\\s*,\\s*";
    private static final String DEFAULT_VALUE_PREFIX = "literal";
    private static final String DEFAULT_CONDITION_PREFIX = "prop";

    private final BindingSource bindingSource;
    private final TypeCoercer resolver;

    public CondBindingFactory(BindingSource bindingSource, TypeCoercer resolver) {
        this.bindingSource = bindingSource;
        this.resolver = resolver;
    }

    @Override
    public Binding newBinding(String description, ComponentResources container, ComponentResources component, String expression, Location location) {
        String[] parts = expression.split(DELIMETER, 3);

        if (parts.length < 3) {
            return bindingSource.newBinding(description, container, component, "prop", expression, location);
        }

        String condition = parts[0];
        String valueTrue = parts[1];
        String valueFalse = parts[2];

        return new CondBinding(makeBinding(condition, DEFAULT_CONDITION_PREFIX, container, description),
                makeBinding(valueTrue, DEFAULT_VALUE_PREFIX, container, description),
                makeBinding(valueFalse, DEFAULT_VALUE_PREFIX, container, description), resolver);
    }

    private Binding makeBinding(String expression, String defaultPrefix, ComponentResources container, String description) {
        String prefix = defaultPrefix;
        String reference = "";

        if (!StringUtils.isEmpty(expression)) {
            String[] parts = expression.split("\\s*:\\s*", 1);

            prefix = parts.length == 2 ? parts[0] : defaultPrefix;
            reference = parts.length == 2 ? parts[1] : parts[0];
        }

        return bindingSource.newBinding(description, container, prefix, reference);
    }
}
