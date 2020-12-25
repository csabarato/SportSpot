package com.sportspot.sportspot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String toDateString(Date date) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        return dateFormatter.format(date);
    }

    public static String toTimeString(Date date) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormatter.format(date);
    }

    public static String toDateTimeString(Date date) {
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return dateTimeFormatter.format(date);
    }

}
