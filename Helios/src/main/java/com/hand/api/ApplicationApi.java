package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicconstant.ApiPath;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/7/6
 * @Version 1.0
 **/
public class ApplicationApi extends BaseRequest{


    /**
     *搜索申请单
     * @param employee
     * @param businessCode
     * @return
     * @throws HttpStatusException
     */
    public JsonArray searchApplication(Employee employee, String businessCode) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.MY_APPLICATION_LIST;
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("withBudget", "false");
        datas.put("withCustomForm", "false");
        datas.put("withCustomFormProperty", "false");
        datas.put("withCustomFormValue", "false");
        datas.put("withParticipant", "false");
        datas.put("withUserInfo", "false");
        datas.put("status", "1001&status=1002&status=1003&status=1004&status=1005&status=1006&status=1007&status=1008");
        datas.put("method", "0");
        datas.put("size", "10");
        datas.put("businessCode", businessCode);
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获得可用的差旅申请的表单
     * @param employee
     * @param jobId
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getAvailableform(Employee employee,String jobId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.GETAVAILABLE_BXFORMS;
        Map<String, String> datas = new HashMap<>();
        datas.put("formType","101");
        datas.put("jobId", jobId);
        String res = doGet(url, getHeader(employee.getAccessToken()), datas, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 新建差旅申请单
     * @param employee
     * @param formdetal
     * @param component
     * @param jobId
     * @param userOID
     * @throws HttpStatusException
     */
    public JsonObject createApplication(Employee employee,JsonObject formdetal, FormComponent component, String jobId, String userOID) throws HttpStatusException {
        JsonObject responseEntity=null;
        JsonArray customFormFields = formdetal.get("customFormFields").getAsJsonArray();
        String url = employee.getEnvironment().getUrl()+ ApiPath.NEW_EXPENSE_REPORT;
        JsonArray  custFormValues = new ReimbursementApi().processCustFormValues(employee,formdetal,component);
        formdetal.remove("custFormValues");
        formdetal.remove("customFormFields");
        formdetal.add("custFormValues",custFormValues);
        formdetal.add("customFormFields",customFormFields);
        formdetal.addProperty("visibleUserScope",1001);
        formdetal.addProperty("applicant",(String) null);
        formdetal.addProperty("takeQuota",false);
        formdetal.addProperty("quotaCurrencyCode","");
        formdetal.addProperty("quotaAmount",0);
        formdetal.addProperty("quotaBankCardOID","");
        formdetal.addProperty("referenceApplicationOID","");
        formdetal.addProperty("applicantOID",userOID);
        formdetal.add("travelApplication",new JsonObject());
        String res= doPost(url,getHeader(employee.getAccessToken()),null,formdetal.toString(),null,employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }

}
