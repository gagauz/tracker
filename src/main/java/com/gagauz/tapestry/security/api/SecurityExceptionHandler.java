package com.gagauz.tapestry.security.api;

import com.gagauz.tapestry.security.AbstractCommonHandlerWrapper;
import com.gagauz.tapestry.security.SecurityException;

public interface SecurityExceptionHandler {

    void handle(AbstractCommonHandlerWrapper handlerWrapper, SecurityException cause);

}
