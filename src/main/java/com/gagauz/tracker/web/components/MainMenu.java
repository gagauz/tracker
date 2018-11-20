package com.gagauz.tracker.web.components;

import javax.inject.Inject;

import org.apache.tapestry5.security.AuthenticationService;

public class MainMenu {

    @Inject
    private AuthenticationService authenticationService;

    void onLogout() {
        authenticationService.logout();
    }
}
