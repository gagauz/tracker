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
        writer.writeRaw("<div class=\"progr-bar\">");
        if (estimated > 0) {
            writer.writeRaw("<div class=\"progr-bar-fill\" style=\"width:" + getProgressPercent() + "%\"></div>");
            if (percent)
                writer.writeRaw("<div>" + getProgressPercent() + "%</div>");
            else
                writer.writeRaw("<div>" + toolsService.getTime(estimated) + "</div>");
        } else {
            writer.writeRaw("<div>" + n_e + "</div>");
        }
        writer.writeRaw("</div>");
    }

    private int getProgressPercent() {
        return 100 * progress / estimated;
    }

}
