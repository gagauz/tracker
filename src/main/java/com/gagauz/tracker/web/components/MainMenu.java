package com.gagauz.tracker.web.components;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tapestry.security.LogoutService;

public class MainMenu {

    @Inject
    private LogoutService logoutService;

    void onLogout() {
        logoutService.logout();
    }
}
