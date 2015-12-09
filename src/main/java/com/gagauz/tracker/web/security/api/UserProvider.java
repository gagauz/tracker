package com.gagauz.tracker.web.security.api;

public interface UserProvider<U extends SecurityUser, T extends Credentials> {
    U findByCredentials(T credentials);
}
