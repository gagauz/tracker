package com.gagauz.tracker.beans.cvs;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

public class GitDateFormat extends DateFormat {
    private final Calendar cal = Calendar.getInstance();

    /*
     * From string "Tue Dec 30 18:36:55 2014 +0300"
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        pos.setIndex(0);
        if (source.length() != 30 && source.length() != 29) {
            throw new RuntimeException("Invalid date string length " + source.length());
        }
        int o = source.length() == 30 ? 1 : 0;
        cal.set(Calendar.MONTH, getMonth(source.substring(4, 7)));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(source.substring(8, 9 + o)));
        cal.set(Calendar.HOUR, Integer.parseInt(source.substring(10 + o, 12 + o)));
        cal.set(Calendar.MINUTE, Integer.parseInt(source.substring(13 + o, 15 + o)));
        cal.set(Calendar.SECOND, Integer.parseInt(source.substring(16 + o, 18 + o)));
        cal.set(Calendar.YEAR, Integer.parseInt(source.substring(19 + o, 23 + o)));

        int h = source.charAt(24 + o) == '-' ? Integer.parseInt(source.substring(24 + o, 27 + o)) * 60
                : Integer.parseInt(source.substring(25 + o, 27 + o)) * 60;
        int m = Integer.parseInt(source.substring(27 + o, 29 + o)) + h;
        pos.setIndex(source.length() - 1);
        cal.set(Calendar.ZONE_OFFSET, m * 60000);
        cal.get(Calendar.ZONE_OFFSET);
        return cal.getTime();
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        cal.setTime(date);
        toAppendTo.append(getWeekDay(cal.get(Calendar.DAY_OF_WEEK))).append(' ');
        toAppendTo.append(getMonthName(cal.get(Calendar.MONTH))).append(' ');
        toAppendTo.append(cal.get(Calendar.DAY_OF_MONTH)).append(' ');
        int i = cal.get(Calendar.HOUR_OF_DAY);
        toAppendTo.append(i > 9 ? i : "0" + i).append(':');
        i = cal.get(Calendar.MINUTE);
        toAppendTo.append(i > 9 ? i : "0" + i).append(':');
        i = cal.get(Calendar.SECOND);
        toAppendTo.append(i > 9 ? i : "0" + i).append(' ');
        toAppendTo.append(cal.get(Calendar.YEAR)).append(' ');
        int off = cal.get(Calendar.ZONE_OFFSET) / 60000;
        toAppendTo.append(off > 0 ? '+' : '-');
        int h = Math.abs(off / 60);
        int m = Math.abs(off % 60);
        toAppendTo.append(h > 9 ? h : "0" + h);
        toAppendTo.append(m > 9 ? m : "0" + m);

        return toAppendTo;
    }

    private int getMonth(String str) {
        if ("Jan".equals(str)) {
            return 0;
        } else if ("Feb".equals(str)) {
            return 1;
        } else if ("Mar".equals(str)) {
            return 2;
        } else if ("Apr".equals(str)) {
            return 3;
        } else if ("May".equals(str)) {
            return 4;
        } else if ("Jun".equals(str)) {
            return 5;
        } else if ("Jul".equals(str)) {
            return 6;
        } else if ("Aug".equals(str)) {
            return 7;
        } else if ("Sep".equals(str)) {
            return 8;
        } else if ("Oct".equals(str)) {
            return 9;
        } else if ("Nov".equals(str)) {
            return 10;
        } else if ("Dec".equals(str)) {
            return 11;
        }
        throw new IllegalStateException("Unknown month " + str);
    }

    private String getWeekDay(int day) {
        switch (day) {
        case 1:
            return "Sun";
        case 2:
            return "Mon";
        case 3:
            return "Tue";
        case 4:
            return "Wed";
        case 5:
            return "Thu";
        case 6:
            return "Fri";
        case 7:
            return "Sat";
        }
        throw new IllegalStateException("Unknown day of week " + day);
    }

    private String getMonthName(int day) {
        switch (day) {
        case 0:
            return "Jan";
        case 1:
            return "Feb";
        case 2:
            return "Mar";
        case 3:
            return "Apr";
        case 4:
            return "May";
        case 5:
            return "Jun";
        case 6:
            return "Jul";
        case 7:
            return "Aug";
        case 8:
            return "Sep";
        case 9:
            return "Oct";
        case 10:
            return "Nov";
        case 11:
            return "Dec";
        }
        throw new IllegalStateException("Unknown month " + day);
    }
}
