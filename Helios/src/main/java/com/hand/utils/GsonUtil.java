package com.hand.utils;

import com.google.gson.*;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
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
    public static <T> String objectToString(T t){
        Gson gson =new Gson();
        return gson.toJson(t);
    }

    /**
     * 反序列化
     * @param json
     * @param t
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
    public static boolean compareJsonObject(JsonObject object1, JsonObject object2, HashMap<String,String> mapping){
        ArrayList<Boolean> arrayList =new ArrayList<>();
        Iterator<String> iterator1 = object1.keySet().iterator();
        //先判断下有没有jsonobject 或者jsonArray 有的话删除
        while (iterator1.hasNext()) {
            String name = iterator1.next();
            if (object1.get(name) instanceof JsonArray || object1.get(name) instanceof JsonObject) {
                object1.remove(name);
                //删除完后成之后必须重新赋值(Iterator 被创建之后会建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会同步改变)
                iterator1 =object1.keySet().iterator();
                log.info("删除的字段为:{}", name);
            }
        }
        Iterator<String> iterator2 = object1.keySet().iterator();
        while(iterator2.hasNext()){
            String name = iterator2.next();
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
            try{
                if(!object1.get(name).isJsonNull() && object2.get(name).isJsonNull()){
                    log.info("数据不一致的字段名:{},value1:{},value2:null",name,object1.get(name));
                    arrayList.add(false);
                    //如果出现了此判断就跳出这一层的循环  不需要在进行判断了
                    continue;
                }
                if(!object1.get(name).getAsString().equals(object2.get(name).getAsString())){
                    //判断如果字段的值不一样  就去映射表去找一下映射关系
                    if(mapping.get(object1.get(name).getAsString())!=null){
                        //
                        if(!mapping.get(object1.get(name).getAsString()).equals(object2.get(name).getAsString())){
                            log.info("数据不一致的字段名:{},value1:{},value2:{}",name,object1.get(name),object2.get(name));
                            arrayList.add(false);
                        }
                    }else{
                        log.info("数据不一致的字段名:{},value1:{},value2:{}",name,object1.get(name),object2.get(name));
                            arrayList.add(false);
                    }
//                    Iterator<String> iterator = mapping.keySet().iterator();
//                    boolean isfind =false;
//                    while (iterator.hasNext()){
//                        String value =iterator.next();
//                        log.info("获取的字符为1:{},获取的字符2:{},映射表中的key:{},映射表中的value:{}",object1.get(name).getAsString(),object2.get(name).getAsString(),value,mapping.get(value));
//                        if(object1.get(name).getAsString().equals(value) && object2.get(name).getAsString().equals(mapping.get(value))){
//                            isfind =true;
//                            break;
//                        }
//                    }
//                    if(!isfind){
//                        log.info("数据不一致的字段名:{},value1:{},value2:{}",name,object1.get(name),object2.get(name));
//                            arrayList.add(false);
//                    }
                }
            }catch (NullPointerException e){
                //空指针异常容易出现在 json1中的key 在json2中不存在 就会出现空异常 然后判断映射表
                log.info("字段{}在json2中不存在,正在查找映射表",name);
                //判断映射表是否存在此映射关系   如果存在的话 就继续判断
                try{
                    if(!mapping.get(name).equals("")){
                        if(!object1.get(name).getAsString().equals(object2.get(mapping.get(name)).getAsString())){
                            log.info("映射数据不一致的字段名:{},value1:{},value2:{}",name,object1.get(name),object2.get(mapping.get(name)));
                            arrayList.add(false);
                        }
                    }else{
                        log.info("无法判断此字段,请手动检查,查不到的key为:{}",name);
                        arrayList.add(false);
                    }
                }catch (NullPointerException e1){
                    log.info("映射表中无此映射,请添加映射关系再试");
                    arrayList.add(false);
                }
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
    public static boolean compareJsonArray(JsonArray array1, JsonArray array2,HashMap mapping){
        ArrayList<Boolean> arrayList = new ArrayList<>();
        for(int i=0;i<array1.size();i++){
            if(array1.get(i).isJsonObject()){
                if(!compareJsonObject(array1.get(i).getAsJsonObject(),array2.get(i).getAsJsonObject(),mapping)){
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
