package com.hand.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author peng.zhang
 * @Date 2020/8/24
 * @Version 1.0
 **/
public class Md5Util {

    // 获取MD5 加密字符
    public static String getMd5(String src) {
        return DigestUtils.md5Hex(src);
    }
}
