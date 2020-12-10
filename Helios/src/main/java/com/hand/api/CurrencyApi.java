package com.hand.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ApiPath;
import com.hand.basicObject.Employee;

import java.util.HashMap;
import java.util.Map;

public class CurrencyApi {
    BaseRequest baseRequest =new BaseRequest();

    public JsonObject queryCurrency(Employee employee, String currencyCode,String language,String page,String size, String setOfBooksId) throws HttpStatusException{
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.QUERY_CURRENCY_RATE);
        Map<String,String> headerParam =new HashMap<>();
        headerParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("baseCurrencyCode",currencyCode);
        urlParam.put("language",language);
        urlParam.put("method",page);
        urlParam.put("size",size);
        urlParam.put("tenantId",employee.getTenantId());
        urlParam.put("setOfBooksId",setOfBooksId);
        urlParam.put("roleType","TENANT");
        JsonObject jsonbody=new JsonObject();
        String res= baseRequest.doGet(url,headerParam,urlParam,employee);
        return  new JsonParser().parse(res).getAsJsonObject();
    }

    public JsonObject updateCurrency(Employee employee, String currencyRateOid, String flag) throws HttpStatusException{
        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.UPDATE_CURRENCY_RATE);
        Map<String,String> headerParam =new HashMap<>();
        headerParam.put("Authorization", "Bearer "+employee.getAccessToken()+"");
        Map<String,String> urlParam=new HashMap<>();
        urlParam.put("currencyRateOid",currencyRateOid);
        urlParam.put("enable",flag);
        urlParam.put("roleType","TENANT");
        String requestBody="{}";
        String res= baseRequest.doPut(url,headerParam,urlParam,requestBody,null,employee);
        return  new JsonParser().parse(res).getAsJsonObject();
    }
}
