package com.gagauz.tracker.web.pages;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.User;

@Secured
public class UserInfo {

    @Property(write = false)
    private User user;

    void onActivate(EventContext context) {
        this.user = context.get(User.class, 0);
    }

}
