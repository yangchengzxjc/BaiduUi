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
    private static final String UTC_FORMATTER_PATTERN16 = "yyyy-MM-dd'T'16:00:00'Z'";
    private static final String UTC_FORMATTER_PATTERN15  ="yyyy-MM-dd'T'15:59:59'Z'";

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

    /**
     * 获取当前UTC的日期   例如：2020-07-06T16:00:00Z   用于申请单添加行程
     * 这种日期格式换成北京时间+8  所以上面 的时间是实际是2020-07-07 00：00：00
     * @return
     */
    public static String getNowStartUtcDate(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN16);
        DateTime date = DateTime.now(DateTimeZone.UTC).plusDays(-1);
        return fmt.print(date);
    }

    /**
     * 获取任意的utc日期  一般用于结束日期
     * @param day
     * @return
     */
    public static String getUtcStartDate(int day){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN16);
        DateTime date = DateTime.now(DateTimeZone.UTC).plusDays(day-1);
        return fmt.print(date);
    }

    /**
     * 这个时间如果需要获取当天的时间则传个0；  时间格式为2020-07-06T15:59:59Z
     * 转换成北京时间则为：2020-07-06 23:59:59
     * @param day
     * @return
     */
    public static String getUTCDateEnd(int day){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN15);
        DateTime date = DateTime.now(DateTimeZone.UTC).plusDays(day);
        return fmt.print(date);
    }


}
