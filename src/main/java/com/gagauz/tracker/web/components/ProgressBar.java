package com.gagauz.tracker.web.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;

public class ProgressBar extends ProgressTime {

    @Parameter(value = "false")
    private boolean percent;

    @Override
    @BeginRender
    void beginRender(MarkupWriter writer) {
        writer.writeRaw("<div class=\"progress\">");
        if (estimate > 0) {
            writer.writeRaw("<div class=\"progress-bar\" style=\"width:" + getProgressPercent() + "%\"></div>");
            if (percent)
                writer.writeRaw("<span>" + getProgressPercent() + "%</span>");
            else
                writer.writeRaw("<span>" + toolsService.getTime(estimate) + "</span>");
        } else {
            writer.writeRaw("<span>" + n_e + "</span>");
        }
        writer.writeRaw("</div>");
    }

    private int getProgressPercent() {
        return 100 * progress / estimate;
    }

}
