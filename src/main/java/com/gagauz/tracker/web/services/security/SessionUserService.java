package com.gagauz.tracker.web.services.security;

public interface SessionUserService {
    SessionUser loadByCredentials(Credentials credentials);
}
