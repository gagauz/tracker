package com.gagauz.tracker.web.services;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.TypeCoercer;

import com.gagauz.tracker.db.model.Role;

public class TypeCoercerModule {

    @Contribute(TypeCoercer.class)
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        configuration.add(CoercionTuple.create(List.class, Set.class, new Coercion<List, Set>() {
            @Override
            public Set coerce(List input) {
                return new HashSet(input);
            }
        }));

        configuration.add(CoercionTuple.create(Collection.class, EnumSet.class, new Coercion<Collection, EnumSet>() {

            @Override
            public EnumSet coerce(Collection input) {
                return EnumSet.copyOf(input);
            }
        }));

        configuration.add(CoercionTuple.create(Collections.<String>emptyList().getClass(), Role[].class,
                new Coercion<List<String>, Role[]>() {

                    @Override
                    public Role[] coerce(List<String> input) {
                        if (null == input || input.isEmpty()) {
                            return Role.EMPTY_ARRAY;
                        }
                        Role[] array = new Role[input.size()];
                        for (int i = 0; i < input.size(); i++) {
                            array[i] = Role.valueOf(input.get(i));
                        }
                        return array;
                    }
                }));
    }
}
