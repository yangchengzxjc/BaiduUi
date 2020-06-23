package com.hand.utils;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;

import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2019/11/5
 * @Version 1.0
 **/

public class GsonUtil {
        /**
         * 序列化为json字符串
         */
        public <T> String  objectToString(T tClass){

            Gson gson =new Gson();
            return gson.toJson(tClass);
        }

        public <T> T stringToObject(String json, Class<T> t){
            Gson gson =new Gson();
            return gson.fromJson(json,t);
        }

    public static List<String> JsonExtractor(Object obj, String jsonParth) {
        String str = obj.toString();
        List<String> titless = null;
        try {
            titless = JsonPath.read(str, jsonParth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titless;
    }
}
