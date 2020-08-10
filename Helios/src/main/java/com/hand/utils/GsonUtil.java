package com.hand.utils;

import com.google.gson.*;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author peng.zhang
 * @Date 2019/11/5
 * @Version 1.0
 **/
@Slf4j
public class GsonUtil {
        /**
         * 序列化为json字符串
         */
        public static <T> String  objectToString(T tClass){

            Gson gson =new Gson();
            return gson.toJson(tClass);
        }

    /**
     * 反序列化
     * @param json
     * @param t
     * @param <T>
     * @return
     */
        public static <T> T stringToObject(String json, Class<T> t){
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
        if(isNotEmpt(array)){
            for(int i=0;i<array.size();i++){
                if(array.get(i).getAsJsonObject().get(key1).getAsString().equals(value)){
                    value2 = array.get(i).getAsJsonObject().get(key2).getAsString();
                }
            }
        }
         return value2;
    }

    /**
     * 根据key相同的value的话 返回的是一个object
     * @param array
     * @param key1
     * @param value
     * @return
     */
    public static JsonObject getJsonValue(JsonArray array, String key1,String value){
        JsonObject object =new JsonObject();
        if(isNotEmpt(array)){
            for(int i=0;i<array.size();i++){
                if(array.get(i).getAsJsonObject().get(key1).getAsString().equals(value)){
                    object =array.get(i).getAsJsonObject();
                }
            }
        }
        return object;
    }

    /**
     * json字符串转为json对象
     * @param jsonString
     */
    public static JsonObject stringToJsonObject(String jsonString){
         return new JsonParser().parse(jsonString).getAsJsonObject();
    }

    /**
     * json字符串转为json数组
     * @param jsString
     * @return
     */
    public static JsonArray stringToJsonArray(String jsString){
        return new JsonParser().parse(jsString).getAsJsonArray();
    }

    /**
     * 判断Json 数组是否为空
     * @param array
     * @return
     */
    public static boolean isNotEmpt(JsonArray array){
        return array.size() != 0;
    }

    /**
     * 比较两个jsonObject 是否相同
     * @param object1
     * @param object2
     * @return
     */
    public static boolean compareJsonObject(JsonObject object1,JsonObject object2){
        ArrayList<Boolean> arrayList =new ArrayList<>();
        Iterator<String> iterator1 = object1.keySet().iterator();
        while(iterator1.hasNext()){
            String name=iterator1.next();
            if(object1.get(name).isJsonArray() && !object2.get(name).isJsonArray()){
                arrayList.add(false);
                System.out.println(name);
                break;
            }
            if(object1.get(name).isJsonObject() && !object2.get(name).isJsonObject()){
                arrayList.add(false);
                System.out.println(object1.get(name));
                break;
            }
            if(object1.get(name) instanceof JsonArray || object1.get(name) instanceof JsonObject){
                object1.remove(name);
                object2.remove(name);
                iterator1=object1.keySet().iterator();
                System.out.println(object1+"  "+name);
                continue;
            }
            if(!object1.get(name).getAsString().equals(object2.get(name).getAsString())){
                arrayList.add(false);
            }
        }
        return arrayList.size() == 0;
    }

    /**
     * 比较俩个json数组是否相同  以第一个jsonArray为基准 在第二个jsonAsrray中查找
     * @param array1
     * @param array2
     * @return
     */
    public boolean compareJsonArray(JsonArray array1, JsonArray array2){
        ArrayList<Boolean> arrayList = new ArrayList<>();
        for(int i=0;i<array1.size();i++){
            if(array1.get(i).isJsonObject()){
                if(!compareJsonObject(array1.get(i).getAsJsonObject(),array2.get(i).getAsJsonObject())){
                    arrayList.add(false);
                    log.info("不同的json对象是:{}",array1.get(i));
                }
            }else if(!array1.get(i).getAsString().equals(array2.get(i).getAsString())){
                arrayList.add(false);
            }
        }
        return arrayList.size()==0;
    }
}
