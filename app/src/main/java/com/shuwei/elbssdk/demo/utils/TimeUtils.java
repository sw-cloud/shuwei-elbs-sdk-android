package com.shuwei.elbssdk.demo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
public class TimeUtils {

    static int MILLIS_TIME_LENGTH = 13;

    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
    };

    public static String[] getLegalDateTime(long timestamp) {
        timestamp = getLegalTimestamp(timestamp);
        String dataTimeStr = DATE_FORMAT.get().format(new Date(timestamp));
        return dataTimeStr.split(" ");
    }

    public static long getLegalTimestamp(long timestamp) {
        if (timestamp == 0 || String.valueOf(timestamp).length() < MILLIS_TIME_LENGTH) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
