package com.gagauz.tracker.db.base;

import com.gagauz.tracker.db.model.Attachment;
import com.gagauz.tracker.utils.TextTransformer;

import java.util.HashMap;
import java.util.Map;

public class TextTransformers {
    private static final Map<Class<?>, TextTransformer<?>> map = new HashMap<Class<?>, TextTransformer<?>>();

    static {
        map.put(Attachment.class, new TextTransformer<Attachment>() {

            @Override
            public String applyP(Attachment param) {
                return param.getValue();
            }

            @Override
            public Attachment applyR(String param) {
                return new Attachment(param);
            }
        });
    }

    public static TextTransformer<?> get(Class<?> clazz) {
        return map.get(clazz);
    }
}
