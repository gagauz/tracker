package org.gagauz.tapestry.common.components;

import org.apache.tapestry5.annotations.Parameter;

public class FormSupportInterceptor {

    @Parameter
    private Runnable action;

    void setupRender() {
        System.out.println("=== FormSupportInterceptor.setupRender() " + action);
        if (null != action) {
            action.run();
        }
    }
}
