package com.gagauz.tracker.web.security;

import com.gagauz.tracker.db.model.Roles;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Secured {
    Roles[] value() default {};
}
