package com.gagauz.tapestry.encoder;

import com.gagauz.tapestry.components.MultiSelect;
import org.apache.tapestry5.ValueEncoder;

import java.util.Collection;

/**
 * CollectionValueEncoder implementation of ValueEncoder special for {@link MultiSelect}
 * to handle multiple values.
 * 
 * @author mg
 *
 * @param <T>
 */

public class CollectionValueEncoder1<V> implements ValueEncoder<Collection<V>> {

    public CollectionValueEncoder1(ValueEncoder<V> encoder) {

    }

    @Override
    public String toClient(Collection value) {
        return null;
    }

    @Override
    public Collection toValue(String clientValue) {
        // TODO Auto-generated method stub
        return null;
    }

}
