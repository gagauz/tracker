package com.gagauz.tracker.web.services;

import org.apache.tapestry5.ioc.Messages;

public class ToolsService {

    private Messages messages;

    private String day;
    private String hour;
    private String minute;

    public ToolsService(Messages messages) {
        this.messages = messages;
        this.day = messages.get("day");
        this.hour = messages.get("hour");
        this.minute = messages.get("minute");
    }

    public String getTime(int time) {
        if (time == 0) {
            return "0";
        }
        StringBuffer sb = new StringBuffer();

        if (time > 1440) {
            sb.append(time / 1440).append(day).append(" ");
            time = time % 1440;
        }

        if (time > 60) {
            sb.append(time / 60).append(hour).append(" ");
            time = time % 60;
        }
        if (time > 0) {
            sb.append(time).append(minute).append(" ");
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
