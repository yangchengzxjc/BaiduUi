package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.baseMethod.Base;
import com.hand.basicConstant.ApiPath;
import com.hand.basicConstant.BaseConstant;
import com.hand.basicConstant.HeaderKey;
import com.hand.basicConstant.ResourceId;
import com.hand.basicObject.Employee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FinanceApi extends BaseRequest{


    /**
     * 报销单财务审核通过
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException

     */
    public JsonObject reportAuditpass(Employee employee, String  expenseReportOID, int entityType) throws HttpStatusException {
        JsonObject responseEntity=null;
        String url = employee.getEnvironment().getUrl()+ ApiPath.EXPENSEREPORT_AUDITPASS;
        JsonObject body = new JsonObject();
        JsonObject expenseReport = new JsonObject();
        JsonArray entities = new JsonArray();
        expenseReport.addProperty("entityOID",expenseReportOID);
        expenseReport.add("attachmentOIDs",new JsonArray());
        expenseReport.addProperty("entityType",entityType);
        expenseReport.add("countersignApproverOIDs",new JsonArray());
        entities.add(expenseReport);
        body.addProperty("approvalTxt","通过");
        body.add("conditionIds",new JsonArray());
        body.add("entities",entities);
        body.addProperty("ignoreCheck",true);
        String res = doPost(url,getHeader(employee.getAccessToken(),HeaderKey.FINANCE_AUDIT, ResourceId.FINANCE_AUDIT),null,body.toString(),null,  employee);
        responseEntity = new JsonParser().parse(res).getAsJsonObject();
        return  responseEntity;
    }


    /**
     * 报销单财务审核拒绝
     * @param employee
     * @param expenseReportOID
     * @return
     * @throws HttpStatusException
     */
    public  JsonObject reportAuditreject(Employee employee, String  expenseReportOID, int entityType) throws HttpStatusException {
        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSEREPORT_AUDITREJECT;
        JsonObject body=new JsonObject();
        JsonObject expenseReport=new JsonObject();
        JsonArray entities=new JsonArray();
        expenseReport.addProperty("entityOID",expenseReportOID);
        expenseReport.addProperty("entityType",entityType);
        expenseReport.add("attachmentOIDs",new JsonArray());
        entities.add(expenseReport);
        body.addProperty("approvalTxt","驳回");
        body.add("entities",entities);
        body.add("conditionIds",new JsonArray());
        body.addProperty("needChargeBack",false);
        String res= doPost(url,getHeader(employee.getAccessToken(), HeaderKey.FINANCE_AUDIT, ResourceId.FINANCE_AUDIT),null,body.toString(),null,  employee);
        return new JsonParser().parse(res).getAsJsonObject();

    }



















}


