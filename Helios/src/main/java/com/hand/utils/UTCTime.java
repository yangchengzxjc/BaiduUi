package com.hand.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class UTCTime {

    private static final String UTC_FORMATTER_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * 获取当前utc时间
     * @return
     */
    public static String getNowUtcTime(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN);
        DateTime now = DateTime.now(DateTimeZone.UTC);
        return fmt.print(now);
    }

    /**
     * 为差补获取任意时间
     * @param day
     * @return
     * example:  day ：1  表示明天    day: -1  表示昨天   day: 3  3天后  0表示当前天数
     *           time同上
     */
    public static String getUtcTime(int day, int time){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN);
        DateTime date = DateTime.now(DateTimeZone.UTC).plusDays(day).plusHours(time);
        return fmt.print(date);
    }
}
