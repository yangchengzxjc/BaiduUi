package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ApiPath;
import com.hand.basicObject.Employee;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/6/17
 * @Version 1.0
 **/

@Slf4j
public class ApproveApi extends BaseRequest{

    /**
     * 根据单号查询单据
     * @param employee
     * @param businessCode
     * @return
     * @throws HttpStatusException
     */
    public JsonArray reportSearch(Employee employee,String businessCode) throws  HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.APPROVAL_LIST_SEARCH;
        Map<String, String> datas = new HashMap<>();
        datas.put("entityType","");
        datas.put("departmentOIDs","");
        datas.put("companyOIDs","");
        datas.put("nodeOIDs","");
        datas.put("minAmount", "");
        datas.put("maxAmount", "");
        datas.put("beginDate", "");
        datas.put("endDate","");
        datas.put("finished","false");
        datas.put("businessCode",businessCode);
        datas.put("page", "0");
        datas.put("size", "20");
        String res= doGet(url,getHeader(employee.getAccessToken()),datas,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 员工审批单据
     * @param employee
     * @param approverOID 审批人的userOID
     * @param expenseReportOID
     * @param entityType  表示单据类型   1001：申请单    1002：表示报销单
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject reportApproval(Employee employee,String approverOID, String expenseReportOID, int entityType) throws HttpStatusException {
        JsonObject responseEntity=null;
        String url = employee.getEnvironment().getUrl()+ ApiPath.EXPENSEREPORT_APPROVAL;
        JsonObject jsonObject=new JsonObject();
        JsonArray entities=new JsonArray();
        JsonObject entitie =new JsonObject();
        entitie.addProperty("entityOID",expenseReportOID);
        entitie.addProperty("approverOID",approverOID);
        entitie.addProperty("entityType",entityType);
        entities.add(entitie);
        jsonObject.add("entities",entities);
        jsonObject.addProperty("approvalTxt","审批通过");
        String res= doPost(url,getHeader(employee.getAccessToken()),null,jsonObject.toString(),null,  employee);
        responseEntity=new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }
}


