package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.SetOfBooks;
import com.hand.basicconstant.ApiPath;
import com.hand.utils.RandomNumber;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class SetOfBooksApi extends BaseRequest {
    /*
    * 新增账套
    * */
    public JsonObject addSetOfBooks(Employee employee, SetOfBooks setOfBooks,boolean enabled, String SetOfBooksName, String SetOfBooksCode) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.ADD_SET_OF_BOOKS;
        Random random = new Random();
        random.nextInt();
        JsonObject body = new JsonObject();
        JsonArray array = new JsonArray();
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("roleType","TENANT");
        JsonObject SetOfBooksNameI18n1 =  new JsonObject();
        JsonObject SetOfBooksNameI18n2 =  new JsonObject();
        body.addProperty("periodSetCode",setOfBooks.getPeriodSetCode());
        body.addProperty("accountSetId",setOfBooks.getAccountSetId());
        body.addProperty("functionalCurrencyCode",setOfBooks.getFunctionalCurrencyCode());
        body.addProperty("setOfBooksName",SetOfBooksName + String.valueOf(RandomNumber.getTimeNumber()));
        body.addProperty("setOfBooksCode",SetOfBooksCode + String.valueOf(RandomNumber.getTimeNumber()));
        body.addProperty("enabled",enabled);
        SetOfBooksNameI18n1.addProperty("language","zh_cn");
        SetOfBooksNameI18n1.addProperty("value",RandomNumber.getUUID());
        SetOfBooksNameI18n2.addProperty("language","en");
        SetOfBooksNameI18n2.addProperty("value",RandomNumber.getUUID());
        array.add(SetOfBooksNameI18n1);
        array.add(SetOfBooksNameI18n2);
        String res = doPost(url,getHeader(employee.getAccessToken()),urlParam,body.toString(),null,employee);
        log.info("新增账套数据为：" + res);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /*
     * 获取会计期
     * */
    public JsonArray getAccountingPeriod(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_ACCOUNTING_PERIOD;
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("roleType","TENANT");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        log.info("会计期详情数据：" + res);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /*
    * 获取科目表
    * */
    public JsonObject getAccountSetId(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_LEDGER_ACCOUNT;
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("roleType","TENANT");
        urlParam.put("enabled","true");
        urlParam.put("page","0");
        urlParam.put("size","10000");
        urlParam.put("tenantId",employee.getTenantId());
        urlParam.put("language","zh_cn");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        log.info("获取的科目表详情数据：" + res);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /*
    * 获取本位币
    * */
    public JsonObject getfunctionalCurrencyCode(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_CURRENCY;
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("roleType","TENANT");
        urlParam.put("currencyCode","");
        urlParam.put("language","zh_cn");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        log.info("获取到的币种数据：" + res);
        return new JsonParser().parse(res).getAsJsonObject();
    }
}
