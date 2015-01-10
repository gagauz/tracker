package com.gagauz.tracker.web.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;

public class ProgressBar {

    @Parameter(required = true)
    private int progress;
    @Parameter(required = true)
    private int estimated;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        writer.writeRaw("<div class=\"progr-bar\">");
        if (estimated > 0) {
            writer.writeRaw("<div class=\"progr-bar-fill\" style=\"width:" + getProgressPercent() + "%\"></div>");
            writer.writeRaw("<div>" + getProgressPercent() + "%</div>");
        } else {
            writer.writeRaw("<div>N/A</div>");
        }
        writer.writeRaw("</div>");
    }

    private int getProgressPercent() {
        return 100 * progress / estimated;
    }

}
