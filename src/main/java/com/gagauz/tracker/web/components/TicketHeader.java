package com.gagauz.tracker.web.components;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.web.services.ToolsService;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class TicketHeader {
    @Component(parameters = {"id=literal:ticketZone"})
    private Zone ticketZone;

    @Parameter(required = true)
    @Property(write = false)
    private Ticket ticket;

    @Property
    private Ticket newTicket;

    @Inject
    protected ToolsService toolsService;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    public String getTime(int min) {
        return toolsService.getTime(min);
    }

    public String getRemaining() {
        return toolsService.getTime(ticket.getEstimate() - ticket.getProgress());
    }

    @Ajax
    void onEdit(Ticket ticket) {
        newTicket = ticket;
        ajaxResponseRenderer
                .addRender(Layout.MODAL_BODY_ID, ticketZone.getBody())
                .addCallback(new JavaScriptCallback() {
                    @Override
                    public void run(JavaScriptSupport javascriptSupport) {
                        javascriptSupport.require("modal").invoke("showModal").with(Layout.MODAL_ID);
                    }
                });
    }
}
