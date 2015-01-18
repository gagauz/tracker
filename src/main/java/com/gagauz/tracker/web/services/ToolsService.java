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
