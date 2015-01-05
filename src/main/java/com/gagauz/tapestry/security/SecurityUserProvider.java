package com.gagauz.tapestry.security;

public interface SecurityUserProvider {
    SecurityUser loadByCredentials(Credentials credentials);
}
