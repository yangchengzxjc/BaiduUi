package com.hand.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.BaseConstant;
import com.hand.basicconstant.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class LoginApi extends BaseRequest{


    public JsonObject login (Employee employee){
        JsonObject responseEntity=null;
        String url= employee.getEnvironment().getUrl()+ ApiPath.GET_TOKEN;
        Map<String, String> headersdatas = new HashMap<>();
        headersdatas.put("Authorization", BaseConstant.AUTHORIZATION);
        headersdatas.put("Content-Type", BaseConstant.CONTENT_TYPE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", employee.getUserName());
        data.put("password", employee.getPassWord());
        data.put("grant_type", "password");
        data.put("scope", "write");
        try {
            String res = doPost(url,headersdatas,null,null,data,employee);
            responseEntity=new JsonParser().parse(res).getAsJsonObject();
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        return  responseEntity;
    }


}
