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
    private static final String UTC_FORMATTER_PATTERN15  = "yyyy-MM-dd'T'15:59:59'Z'";
    private static final String BEiJING_FORMATTER_PATTERN15  = "yyyy-MM-dd";
    private static final String BEIJING_FORMATTER_TIME = "yyyy-MM-dd HH:mm:ss";
    private static final String BEIJING_TIME = "HH:mm";
    private static final String BEIJING_TIME2 = "yyyyMMdd";

    /**
     * 返回北京时间的任意日期
     * @param day
     * @return
     */
    public static String getBeijingDate(int day){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(BEiJING_FORMATTER_PATTERN15);
        DateTime date = DateTime.now().plusDays(day);
        return fmt.print(date);
    }

    /**
     * 返回北京时间   例如：  13:30
     * @param hour  0为当前小时
     * @param minute  0 为当前分钟
     * @return
     */
    public static String getTime(int hour,int minute){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(BEIJING_TIME);
        DateTime time = DateTime.now().plusHours(hour).plusMinutes(minute);
        return fmt.print(time);
    }


    /**
     * 获取北京时间如果是当前时间  则 day  和time  传0  以此类推
     * @param day 当前时间0  明天+1,昨天-1
     * @param time 同上
     * @return
     */
    public static String getBeijingTime(int day, int time){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(BEIJING_FORMATTER_TIME);
        DateTime Bejingtime = DateTime.now().plusDays(day).plusHours(time);
        return fmt.print(Bejingtime);
    }
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
     * 获取当前的北京时间   格式为：20200828   用于消费计算费用生成批次
     *
     * @return
     */
    public static String getBeijingDay(int day){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(BEIJING_TIME2);
        DateTime date = DateTime.now().plusDays(day);
        return fmt.print(date);
    }

    /**
     * 获取任意的utc开始结束日期日期  一般用于结束日期
     * @param day
     * @return
     */
    public static String getUtcStartDate(int day){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN16);
        DateTime date = DateTime.now(DateTimeZone.UTC).plusDays(day-1);
        return fmt.print(date);
    }

    /**
     * 这个时间如果需要获取当天的时间则传个0;时间格式为2020-07-06T15:59:59Z
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
