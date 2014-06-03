package org.kesler.simplereg.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 29.05.14.
 */
public class DateUtil {

    public static Date toBeginOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date!=null) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            date = calendar.getTime();
        }
        return date;
    }

    public static Date toEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date!=null) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            date = calendar.getTime();
        }
        return date;
    }
}
