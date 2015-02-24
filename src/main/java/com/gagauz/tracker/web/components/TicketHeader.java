package com.gagauz.tracker.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.web.services.ToolsService;

public class TicketHeader {
    @Parameter(required = true)
    @Property(write = false)
    private Ticket ticket;

    @Inject
    protected ToolsService toolsService;

    public String getTime(int min) {
        return toolsService.getTime(min);
    }

    public String getRemaining() {
        return toolsService.getTime(ticket.getEstimate() - ticket.getProgress());
    }
}
