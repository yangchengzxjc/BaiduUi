package com.hand.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * @Author peng.zhang
 * @Date 2020/7/2
 * @Version 1.0
 **/
@Slf4j
public class RandomNumber {

    /**
     * 返回UUID字符串 总共32个长度 所以可选长度为1-32  为了避免重复 请尽量选择打的长度
     * @return
     */
    public static String getUUID(int length){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        StringBuffer newUUid =new StringBuffer();
        if(length<=1 || length>32){
            log.info("无法生成字符串,请修改生成的字符串的长度1-32之间");
        }else {
            for (int i = 0; i < length; i++) {
                newUUid.append(uuid.charAt(i));
            }
        }
        return newUUid.toString();
    }

    /**
     * 返回5位数的随机数
     * @return
     */
    public static int getRandomNumber(){
        return (int) (Math.random()*90000+10000);
    }

    /**
     * 返回两数之间的随机数
     * @param number1
     * @param number2
     * @return
     */
    public static int getRandomNumber(int number1, int number2){
        Random random =new Random();
        return random.nextInt(number2-number1+1)+number1;
    }

    /**
     * 返回两数之间的随机数  保留两位小数
     * @return
     */
    public static BigDecimal getDoubleNumber(int number1, int number2){
        return new BigDecimal(Math.random()*(number2-number1)+number1).setScale(2,ROUND_DOWN);
    }

    /**
     * 通过时间戳的形式获取一个独一无二的数
     * @return
     */
    public static String getTimeNumber(){
        Date date= new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        return dateFormat.format(date);
    }

    public static String getTimeNumber(int length){
        long timeNumber = System.currentTimeMillis()*getRandomNumber(10000,99999);
        String randoNumber =String.valueOf(timeNumber);
        StringBuffer number =new StringBuffer();
        if(length<=1 || length>15){
            log.info("无法生成随机数,请修改生成的字符串的长度1-15之间");
        }else {
            for (int i = 0; i < length; i++) {
                number.append(randoNumber.charAt(i));
            }
        }
        return number.toString();
    }
}
