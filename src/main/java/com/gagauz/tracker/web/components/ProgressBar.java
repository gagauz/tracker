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
            writer.writeRaw("<div aria-valuenow=\"" + getProgressPercent() + "\" aria-valuemin=\"0\" aria-valuemax=\"100\""
                    + " class=\"progress-bar\" style=\"min-width:2em;width:" + getProgressPercent() + "%\">");
            if (percent)
                writer.writeRaw(getProgressPercent() + "%");
            else
                writer.writeRaw(toolsService.getTime(estimate));

            writer.writeRaw("</div>");
        } else {
            writer.writeRaw(n_e);
        }

        writer.writeRaw("</div>");
    }

    private int getProgressPercent() {
        return 100 * progress / estimate;
    }

}
