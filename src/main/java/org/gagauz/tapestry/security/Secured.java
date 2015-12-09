package org.gagauz.tapestry.security;

public @interface Secured {

    String[] value() default {};

}
