package com.hand.utils;

import com.hand.basicconstant.BaseConstant;
import java.io.*;
import java.util.Properties;

/**
 * @Author ： xinghong.chen
 * @Email: xinghong.chen@huilianyi.com
 * @DATE : 2019/3/5 18:08
 **/

public class PropertyReader {
    private static Properties properties = new Properties();
    // 读取配置文件
    public static  Properties getProperties(String propertiesUrl) throws IOException {
        InputStream inputStream = new FileInputStream( propertiesUrl );
        InputStreamReader inputStreamReader = new InputStreamReader( inputStream, BaseConstant.CHARSET_NAME );
        BufferedReader bufferedReader  = new  BufferedReader( inputStreamReader );
        properties.load( bufferedReader );
        return properties;
    }

    // 根据配置文件中的key值得到对应的value值
    public static String getValue(String key) throws IOException{
        return properties.getProperty( key );
    }
}
