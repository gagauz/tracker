package com.gagauz.tracker.web.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;

public class ProgressBar extends ProgressTime {

    @Override
    @BeginRender
    void beginRender(MarkupWriter writer) {
        writer.writeRaw("<div class=\"progr-bar\">");
        if (estimated > 0) {
            writer.writeRaw("<div class=\"progr-bar-fill\" style=\"width:" + getProgressPercent() + "%\"></div>");
            writer.writeRaw("<div>" + getTime(estimated) + "</div>");
        } else {
            writer.writeRaw("<div>" + n_e + "</div>");
        }
        writer.writeRaw("</div>");
    }

    private int getProgressPercent() {
        return 100 * progress / estimated;
    }

}
