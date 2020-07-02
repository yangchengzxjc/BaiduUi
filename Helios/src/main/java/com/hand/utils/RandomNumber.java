package com.hand.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @Author peng.zhang
 * @Date 2020/7/2
 * @Version 1.0
 **/
public class RandomNumber {

    /**
     * 返回UUID字符串
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 返回5位数的小数
     * @return
     */
    public static int getRandomNumber(){
        Random random =new Random(10000);
        return (int) (Math.random()*9000+new Random(0000).nextInt());

    }
}
