package com.hand.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @Author peng.zhang
 * @Date 2020/7/2
 * @Version 1.0
 **/
@Slf4j
public class RandomNumber {

    /**
     * 返回UUID字符串 总共32个长度
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
     * 返回5位数的小数
     * @return
     */
    public static int getRandomNumber(){
        return (int) (Math.random()*90000+10000);
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
}
