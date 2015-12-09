package com.gagauz.tracker.web.security.api;

import com.gagauz.tracker.web.security.AbstractCommonHandlerWrapper;
import com.gagauz.tracker.web.security.AccessDeniedException;

public interface AccessDeniedHandler {
    void handleException(AbstractCommonHandlerWrapper handler, AccessDeniedException cause);
}
