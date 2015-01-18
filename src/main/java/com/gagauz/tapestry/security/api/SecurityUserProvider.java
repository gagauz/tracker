package com.gagauz.tapestry.security.api;


public interface SecurityUserProvider {
    SecurityUser loadByCredentials(Credentials credentials);
}
