package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.ApiPath;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/8
 * @Version 1.0
 **/
@Slf4j
public class EmployeeInfoApi extends BaseRequest {

    /**
     * 获取用户的可用信息
     */
    public JsonObject getEmployeeInfo(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_ACCOUNT;
        String res = doGet(url, getHeader(employee.getAccessToken()), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取当前登录用户公司详情
     */
    public JsonObject getCompanys(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GETCOMPANIES;
        String res = doGet(url, getHeader(employee.getAccessToken()), null, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 查询账户的租户信息
     * @param employee
     * @throws HttpStatusException
     */
    public JsonObject getTenantInfo(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GETTENANTINFO;
        HashMap<String,String> map = new HashMap<>();
        map.put("tenantId",employee.getTenantId());
        String res = doGet(url, getHeader(employee.getAccessToken()), map, employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }


    /**
     * 获取值列表信息
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public  JsonArray getCustomEnumerationOID( Employee employee,String customEnumerationOID) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GET_CUSTOMENUMERATIONOID,customEnumerationOID);
        String res= doGet(url,getHeader(employee.getAccessToken()),null,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


}
