package com.gagauz.tapestry.binding;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.services.TypeCoercer;

public class CondBinding extends AbstractBinding {
    private final Binding conditionBinding;
    private final Binding trueBinding;
    private final Binding falseBinding;
    private final TypeCoercer resolver;

    public CondBinding(Binding conditionBinding, Binding trueBinding, Binding falseBinding, TypeCoercer resolver) {
        this.trueBinding = trueBinding;
        this.falseBinding = falseBinding;
        this.conditionBinding = conditionBinding;
        this.resolver = resolver;
    }

    @Override
    public Object get() {
        String valueTrue = resolver.coerce(trueBinding.get(), String.class);
        String valueFalse = resolver.coerce(falseBinding.get(), String.class);
        Boolean condition = resolver.coerce(conditionBinding.get(), Boolean.class);
        return condition != null && condition ? valueTrue : valueFalse;
    }
}
