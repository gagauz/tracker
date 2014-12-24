package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import java.util.Date;

/**
 * Start page of application tracker.
 */
public class Index {

    @Component
    private Zone zone;

    @Property
    @Inject
    @Symbol(SymbolConstants.TAPESTRY_VERSION)
    private String tapestryVersion;

    @Persist
    @Property
    private int clickCount;

    @Inject
    private AlertManager alertManager;

    @Inject
    private UserDao userDao;

    void onActivate() {
        User user = userDao.findById(1);
        System.out.println(user);
    }

    public Date getCurrentTime()
    {
        return new Date();
    }

    void onActionFromIncrement()
    {
        alertManager.info("Increment clicked");

        clickCount++;
    }

    Object onActionFromIncrementAjax()
    {
        clickCount++;

        alertManager.info("Increment (via Ajax) clicked");

        return zone;
    }
}
