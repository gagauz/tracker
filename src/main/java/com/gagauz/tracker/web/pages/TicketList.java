package com.gagauz.tracker.web.pages;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.web.services.security.Secured;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;

@Secured
public class TicketList {

    @Property
    private Feature feature;

    @Property(write = false)
    private Version version;

    Object onActivate(Version version) {
        if (null == version) {
            return Index.class;
        }

        this.version = version;

        return null;
    }

    public List<Ticket> getTickets() {
        return Collections.emptyList();
    }

}
