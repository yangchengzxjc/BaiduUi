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
    /**
     * 新增账套
     * @param employee
     * @param setOfBooks 账套字段对象
     * @param enabled
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addSetOfBooks(Employee employee, SetOfBooks setOfBooks,boolean enabled) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.ADD_SET_OF_BOOKS;
        Random random = new Random();
        random.nextInt();
        JsonObject body = new JsonObject();
        //账套名称多语言数组
        JsonArray arrayLanguage = new JsonArray();
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("roleType","TENANT");
        //账套名称多语言对象
        JsonObject objectSetOfBooksName = new JsonObject();
        JsonObject setOfBooksNameI18n1 =  new JsonObject();
        JsonObject setOfBooksNameI18n2 =  new JsonObject();
        body.addProperty("periodSetCode",setOfBooks.getPeriodSetCode());
        body.addProperty("accountSetId",setOfBooks.getAccountSetId());
        body.addProperty("functionalCurrencyCode",setOfBooks.getFunctionalCurrencyCode());
        body.addProperty("setOfBooksName",setOfBooks.getSetOfBooksName());
        body.addProperty("setOfBooksCode",setOfBooks.getSetOfBooksCode());
        body.addProperty("enabled",enabled);
        setOfBooksNameI18n1.addProperty("language","zh_cn");
        setOfBooksNameI18n1.addProperty("value",setOfBooks.getSetOfBooksName());
        setOfBooksNameI18n2.addProperty("language","en");
        setOfBooksNameI18n2.addProperty("value",setOfBooks.getSetOfBooksName());
        arrayLanguage.add(setOfBooksNameI18n1);
        arrayLanguage.add(setOfBooksNameI18n2);
        objectSetOfBooksName.add("setOfBooksName",arrayLanguage);
        body.add("i18n",objectSetOfBooksName);
        String res = doPost(url,getHeader(employee.getAccessToken()),urlParam,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }

    /**
     * 获取会计期
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getAccountingPeriod(Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GET_ACCOUNTING_PERIOD;
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("roleType","TENANT");
        String res = doGet(url,getHeader(employee.getAccessToken()),urlParam,employee);
        log.info("会计期详情数据：" + res);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取科目表
     * @param employee
     * @return
     * @throws HttpStatusException
     */
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

    /**
     * 获取本位币
     * @param employee
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getFunctionalCurrencyCode(Employee employee) throws HttpStatusException {
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
