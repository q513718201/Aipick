package com.vinsonguo.klinelib.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String formatDate(long time, String format) {
        DateFormat dateFormat2 = new SimpleDateFormat(format, Locale.getDefault());
        String formatDate = dateFormat2.format(time);
        return formatDate;
    }

    public static long date2Long(String date, String form) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(form, Locale.getDefault());
        try {
            Date parse = simpleDateFormat.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static long date2Long(String date) {
        try {
            if (date.length() == 10) {
                return date2Long(date, "yyyy-MM-dd");
            } else if (date.length() == 7) {
                return date2Long(date, "yyyy-MM");
            } else {
                return date2Long(date, "yyyy-MM-dd HH:mm");
            }
        } catch (Exception e) {
            return 0l;
        }
    }


    public static String formatDate(long time) {
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatDate = dateFormat2.format(time);
        return formatDate;
    }


    public static String formatDateTime(long date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formatDate = dateFormat.format(date);
        return formatDate;
    }


    public static String formatTime(long millis) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formatDate = dateFormat.format(millis);
        return formatDate;
    }

    public static String formatPeriodTime(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        return dateFormat.format(time);
    }

    public static String formatDayTime(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        return dateFormat.format(time);
    }

    /**
     * 获取当前时间days天的时间
     *
     * @param days
     * @return
     */
    public static String getDay(int days) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, days);
        return formatDate(instance.getTimeInMillis());
    }


}
