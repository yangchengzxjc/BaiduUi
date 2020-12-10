package com.hand.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.MyResponse;
import com.hand.baseMethod.OkHttpUtils;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.BaseConstant;
import com.hand.basicObject.Employee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
@Data
public class BaseRequest {

    /**
     * 用户登录获取token
     *
     * @param employee
     * @return
     */
    public JsonObject getToken(Employee employee) throws HttpStatusException {
        JsonObject responseEntity = null;
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_TOKEN;
        Map<String, String> headersdatas = new HashMap<String, String>();
        headersdatas.put("Authorization", BaseConstant.AUTHORIZATION);
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", employee.getPhoneNumber());
        data.put("password", employee.getPassWord());
        data.put("grant_type", "password");
        data.put("scope", "read write");
        String res = doPost(url, headersdatas, null, null, data, employee);
        responseEntity = new JsonParser().parse(res).getAsJsonObject();

        return responseEntity;
    }

    /**
     * 对put方法的二次包装，这里需要有重试机制
     *
     * @param url           url
     * @param headersParams 请求头参数
     * @param urlMapParams  url参数
     * @param jsonbody      JSON参数提
     * @param bodyParams    Form消息体
     * @return
     */
    public String doPut(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, String jsonbody, Map<String, String> bodyParams, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.put(url, headersParams, urlMapParams, jsonbody, bodyParams);
        int code = myResponse.getStatusCode();
        String res = myResponse.getBody();
        switch (code) {
            case HttpStatus.OK_200:
            case HttpStatus.CREATED_201:
                return res;
            case HttpStatus.UNAUTHORIZED_401:
                String newToken = getToken(employee).get("access_token").getAsString();
                log.info("使用新Token：" + newToken);
                employee.setAccessToken(newToken);
                headersParams.put("Authorization", "Bearer " + employee.getAccessToken() + "");
                myResponse = OkHttpUtils.put(url, headersParams, urlMapParams, jsonbody, bodyParams);
                return myResponse.getBody();
            case HttpStatus.GATEWAY_TIMEOUT_504:
            case HttpStatus.BAD_GATEWAY_502:
                myResponse = OkHttpUtils.put(url, headersParams, urlMapParams, jsonbody, bodyParams);
                return myResponse.getBody();
            default:
                log.info("未知错误，还需定位");
        }

        if (HttpStatus.BAD_REQUEST_400 == code && res.contains("baseRequest speed is too fast")) {
            myResponse = OkHttpUtils.put(url, headersParams, urlMapParams, jsonbody, bodyParams);
            res = myResponse.getBody();
        }
        return res;

    }


    /**
     * 对post方法的二次包装，这里需要有重试机制
     *
     * @param url           url
     * @param headersParams 请求头参数
     * @param urlMapParams  url参数
     * @param jsonBody      JSON参数提
     * @param bodyParams    Form消息体
     * @return
     */
    public String doPost(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, String jsonBody, Map<String, String> bodyParams, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.post(url, headersParams, urlMapParams, jsonBody, bodyParams);
        int code = myResponse.getStatusCode();
        String res = myResponse.getBody();
//        log.info("res code: {}", code);
//        log.info("res data: {}", res);
        switch (code) {
            case HttpStatus.OK_200:
            case HttpStatus.CREATED_201:
                return res;
            case HttpStatus.UNAUTHORIZED_401:
                String newToken = getToken(employee).get("access_token").getAsString();
                log.info("使用新Token：" + newToken);
                employee.setAccessToken(newToken);
                headersParams.put("Authorization", "Bearer " + employee.getAccessToken() + "");
                myResponse = OkHttpUtils.post(url, headersParams, urlMapParams, jsonBody, bodyParams);
                return myResponse.getBody();
            case HttpStatus.GATEWAY_TIMEOUT_504:
            case HttpStatus.BAD_GATEWAY_502:
                myResponse = OkHttpUtils.post(url, headersParams, urlMapParams, jsonBody, bodyParams);
                return myResponse.getBody();
            default:
                log.info("未知错误，还需定位");
        }

        if (HttpStatus.BAD_REQUEST_400 == code && res.contains("baseRequest speed is too fast")) {
            myResponse = OkHttpUtils.post(url, headersParams, urlMapParams, jsonBody, bodyParams);
            res = myResponse.getBody();
        }
        return res;

    }


    /**
     * 删除操作
     *
     * @param url
     * @param headersParams
     * @param urlMapParams
     * @param jsonbody
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String doDlete(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, JsonObject jsonbody, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.delete(url, headersParams, urlMapParams, jsonbody);
        int code = myResponse.getStatusCode();
        String res = myResponse.getBody();
        switch (code) {
            case HttpStatus.OK_200:
            case HttpStatus.CREATED_201:
                return res;
            case HttpStatus.UNAUTHORIZED_401:
                String newToken = getToken(employee).get("access_token").getAsString();
                log.info("使用新Token：" + newToken);
                employee.setAccessToken(newToken);
                headersParams.put("Authorization", "Bearer " + employee.getAccessToken() + "");
                myResponse = myResponse = OkHttpUtils.delete(url, headersParams, urlMapParams, jsonbody);
                return myResponse.getBody();
            case HttpStatus.GATEWAY_TIMEOUT_504:
            case HttpStatus.BAD_GATEWAY_502:
                myResponse = myResponse = OkHttpUtils.delete(url, headersParams, urlMapParams, jsonbody);
                return myResponse.getBody();
            default:
                log.info("未知错误，还需定位");
        }

        if (HttpStatus.BAD_REQUEST_400 == code && res.contains("baseRequest speed is too fast")) {
            myResponse = OkHttpUtils.delete(url, headersParams, urlMapParams, jsonbody);
            res = myResponse.getBody();
        }
        return res;
    }

    /**
     * 对get方法的二次包装，这里需要有重试机制
     *
     * @param url           url
     * @param headersParams 请求头参数
     * @param urlMapParams  url参数
     * @return
     */
    public String doGet(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.get(url, headersParams, urlMapParams);
        int code = myResponse.getStatusCode();
        String res = myResponse.getBody();
//        log.info("res code: {}", code);
//        log.info("res data: {}", res);
        switch (code) {
            case HttpStatus.OK_200:
            case HttpStatus.CREATED_201:
                return res;
            case HttpStatus.BAD_REQUEST_400:
                if (res.contains("baseRequest speed is too fast")) {
                    myResponse = OkHttpUtils.get(url, headersParams, urlMapParams);
                    return myResponse.getBody();
                }
            case HttpStatus.UNAUTHORIZED_401:
                String newToken = getToken(employee).get("access_token").getAsString();
                log.info("使用新Token：" + newToken);
                employee.setAccessToken(newToken);
                headersParams.put("Authorization", "Bearer " + employee.getAccessToken() + "");
                myResponse = OkHttpUtils.get(url, headersParams, urlMapParams);
                return myResponse.getBody();
            case HttpStatus.GATEWAY_TIMEOUT_504:
            case HttpStatus.BAD_GATEWAY_502:
                myResponse = OkHttpUtils.get(url, headersParams, urlMapParams);
                return myResponse.getBody();
            default:
                log.info("未知错误，还需定位");
        }
//        Reporter.log("req url: " + url);
//        Reporter.log("req headersParams: " + headersParams.toString());
//        Reporter.log("req urlMapParams: " + urlMapParams.toString());
//        Reporter.log("res code: " + code);
//        Reporter.log("res data: " + res);
        return res;
    }

    /**
     * 上传附件
     *
     * @param url
     * @param headersParams
     * @param bodyParams
     * @param name
     * @param filePath
     * @param fileMediaType
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public String doupload(String url, Map<String, String> headersParams, Map<String, String> bodyParams, String name, String filePath, String fileMediaType, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.UpLoadFile(url, headersParams, bodyParams, name, filePath, fileMediaType);
        int code = myResponse.getStatusCode();
        String res = myResponse.getBody();
        switch (code) {
            case HttpStatus
                    .OK_200:
            case HttpStatus.CREATED_201:
                return res;
            case HttpStatus.UNAUTHORIZED_401:
                String newToken = getToken(employee).get("access_token").getAsString();
                log.info("使用新Token：" + newToken);
                employee.setAccessToken(newToken);
                headersParams.put("Authorization", "Bearer " + employee.getAccessToken() + "");
                myResponse = OkHttpUtils.UpLoadFile(url, headersParams, bodyParams, name, filePath, fileMediaType);
                return myResponse.getBody();
            case HttpStatus.GATEWAY_TIMEOUT_504:
            case HttpStatus.BAD_GATEWAY_502:
                myResponse = myResponse = OkHttpUtils.UpLoadFile(url, headersParams, bodyParams, name, filePath, fileMediaType);
                return myResponse.getBody();
            default:
                log.info("未知错误，还需定位");
        }

        if (HttpStatus.BAD_REQUEST_400 == code && res.contains("baseRequest speed is too fast")) {
            myResponse = OkHttpUtils.UpLoadFile(url, headersParams, bodyParams, name, filePath, fileMediaType);
            res = myResponse.getBody();
        }
        return res;

    }

//    /**
//     *通过公司秘钥与公司clientId登录获取token
//     * @param employee
//     * @return
//     */
//    public  JsonObject getTokenByCompany(Employee employee) throws HttpStatusException,UnsupportedEncodingException {
//        JsonObject responseEntity=null;
//        String url=employee.getEnvironment().getUrl()+ ApiPath.GET_TOKEN;
//        Map<String, String> headersdatas = new HashMap<>();
//        String str = employee.getClientId().toString()+":"+employee.getClientSecret().toString();
//        String authorization = "Basic "+Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
//        headersdatas.put("Authorization", authorization);
//        Map<String, String> data = new HashMap<String, String>();
//        data.put("grant_type", "client_credentials");
//        data.put("scope", "read write");
//        String res= doPost(url,headersdatas,null,null,data,employee);
//        responseEntity=new JsonParser().parse(res).getAsJsonObject();
//        return  responseEntity;
//    }

    public Map<String, String> getHeader(String token) {
        Map<String, String> headersdatas = new HashMap<>();
        headersdatas.put("Authorization", "Bearer " + token);
        headersdatas.put("Content-Type", BaseConstant.CONTENT_TYPE);
        //添加客户端为自动化测试
        headersdatas.put("x-helios-client", "auto test");
        return headersdatas;
    }

    public HashMap<String, String> getHeader(String token, String key) {
        HashMap<String, String> headersdatas = new HashMap<>();
        headersdatas.put("Authorization", "Bearer " + token);
        headersdatas.put("Content-Type", BaseConstant.CONTENT_TYPE);
        headersdatas.put("key", key);
        headersdatas.put("x-helios-client", "auto test");
        return headersdatas;
    }

    /**
     * @param token
     * @param key
     * @param resourceId 各个组的权限认证
     * @return
     */
    public HashMap<String, String> getHeader(String token, String key, String resourceId) {
        HashMap<String, String> headersdatas = new HashMap<>();
        headersdatas.put("Authorization", "Bearer " + token);
        headersdatas.put("Content-Type", BaseConstant.CONTENT_TYPE);
        headersdatas.put("key", key);
        headersdatas.put("resourceId", resourceId);
        headersdatas.put("x-helios-client", "auto test");
        return headersdatas;
    }

    /**
     * 返回get请求的statusCode
     *
     * @param url
     * @param headersParams
     * @param urlMapParams
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public int doGetStatusCode(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.get(url, headersParams, urlMapParams);
        return myResponse.getStatusCode();
    }

    /**
     * 返回post请求的状态码
     *
     * @param url
     * @param headersParams
     * @param urlMapParams
     * @param jsonBody
     * @param bodyParams
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public int getPostStatusCode(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, String jsonBody, Map<String, String> bodyParams, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.post(url, headersParams, urlMapParams, jsonBody, bodyParams);
        return myResponse.getStatusCode();
    }

    /**
     * 返回delete 请求的状态码
     *
     * @param url
     * @param headersParams
     * @param urlMapParams
     * @param jsonbody
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public int getDeleteStatusCode(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, JsonObject jsonbody, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.delete(url, headersParams, urlMapParams, jsonbody);
        return myResponse.getStatusCode();
    }

    /**
     * 返回put请求的状态码
     *
     * @param url
     * @param headersParams
     * @param urlMapParams
     * @param jsonbody
     * @param bodyParams
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public int getPutStatusCode(String url, Map<String, String> headersParams, Map<String, String> urlMapParams, String jsonbody, Map<String, String> bodyParams, Employee employee) throws HttpStatusException {
        MyResponse myResponse = null;
        myResponse = OkHttpUtils.put(url, headersParams, urlMapParams, jsonbody, bodyParams);
        return myResponse.getStatusCode();
    }

}