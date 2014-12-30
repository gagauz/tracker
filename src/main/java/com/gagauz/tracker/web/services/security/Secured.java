package com.gagauz.tracker.web.services.security;

import com.gagauz.tracker.db.model.Role;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Secured {
    Role[] value() default {};
}
