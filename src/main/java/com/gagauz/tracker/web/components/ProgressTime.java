package com.gagauz.tracker.web.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ProgressTime {

    @Parameter(required = true)
    protected int progress;
    @Parameter(required = true)
    protected int estimated;

    @Inject
    private Messages messages;

    private String day = messages.get("day");
    private String hour = messages.get("hour");
    private String minute = messages.get("minute");
    private String done = messages.get("done");
    private String of = messages.get("of");
    protected String n_e = messages.get("n_e");

    @BeginRender
    void beginRender(MarkupWriter writer) {
        writer.writeRaw("<div class=\"progr-time\">");
        if (estimated > 0) {
            if (estimated == progress) {
                writer.writeRaw(done);
            } else {
                writer.writeRaw(getTime(progress));
                writer.writeRaw(of);
                writer.writeRaw(getTime(estimated));
            }
        } else {
            writer.writeRaw(n_e);
        }
        writer.writeRaw("</div>");
    }

    protected String getTime(int time) {
        StringBuffer sb = new StringBuffer();

        if (time > 1440) {
            sb.append(time / 1440).append(day);
            time = time % 1440;
        }

        if (time > 60) {
            sb.append(time / 60).append(hour);
            time = time % 60;
        }
        if (time > 0) {
            sb.append(time).append(minute);
        }

        return sb.toString();
    }

    protected String addZero(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }

}
