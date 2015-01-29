package com.gagauz.tracker.web.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.gagauz.tracker.web.services.ToolsService;

public class ProgressTime {

    @Parameter(required = true)
    protected int progress;

    @Parameter(required = true)
    protected int estimate;

    @Inject
    private Messages messages;

    @Inject
    protected ToolsService toolsService;

    private final String done = messages.get("done");
    private final String of = messages.get("of");
    protected String n_e = messages.get("n_e");

    @BeginRender
    void beginRender(MarkupWriter writer) {
        writer.writeRaw("<div class=\"progr-time\">");
        if (estimate > 0) {
            if (estimate == progress) {
                writer.writeRaw(done);
            } else {
                writer.writeRaw(toolsService.getTime(progress));
                writer.writeRaw(of);
                writer.writeRaw(toolsService.getTime(estimate));
            }
        } else {
            writer.writeRaw(n_e);
        }
        writer.writeRaw("</div>");
    }

}
