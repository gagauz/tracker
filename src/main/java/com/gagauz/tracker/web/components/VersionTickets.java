package com.gagauz.tracker.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.gagauz.tracker.web.utils.AbstractDaoGridDataSource;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.Version;
import com.xl0e.hibernate.utils.EntityFilterBuilder;

public class VersionTickets {
    @Parameter(required = true, allowNull = false)
    private Version version;

    @Property
    private Ticket ticket;

    public GridDataSource getTickets() {
        return new AbstractDaoGridDataSource<>(EntityFilterBuilder.eq("version", version), Ticket.class);
    }

}
