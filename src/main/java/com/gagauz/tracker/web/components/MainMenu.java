package com.gagauz.tracker.web.components;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.gagauz.tapestry.security.LogoutService;

public class MainMenu {

    @Inject
    private LogoutService logoutService;

    void onLogout() {
        logoutService.logout();
    }
}
