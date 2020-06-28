package com.hand.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import org.openqa.selenium.json.Json;

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

    /**
     * 获取和key1有相同的value的层级 来获取key2的value  并返回这个value2
     * @param array json 数组
     * @param key1   用于判断的key1
     * @param value  用于判断的vaue
     * @param key2   需要获取的key2,
     * @return
     */
    public static String getJsonValue(JsonArray array, String key1, String value, String key2){
        String value2="";
        for(int i=0;i<array.size();i++){
            if(array.get(i).getAsJsonObject().get(key1).getAsString().equals(value)){
                value2 = array.get(i).getAsJsonObject().get(key2).getAsString();
            }
        }
         return value2;
    }
}
