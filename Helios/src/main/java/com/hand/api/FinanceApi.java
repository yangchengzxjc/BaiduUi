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

@Slf4j
public class FinanceApi extends BaseRequest{

//    /**
//     *财务审核列表查询
//     * @param employee
//     * @param businessCode
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonArray financialAuditListSearch(Employee employee, String  businessCode,int type) throws HttpStatusException {
//        JsonArray responseEntity=null;
//        String url = null;
//        if (type==BaseConstant.ENTITY_TYPE[1]) {
//            url=employee.getEnvironment().getUrl()+ ApiPath.FINANCIAL_EXPENSE_REORTS_AUDIT_LIST_SEARCH;
//        }
//        else  if (type==BaseConstant.ENTITY_TYPE[0]){
//            url=employee.getEnvironment().getUrl()+ ApiPath.FINANCIAL_APPLICATION_AUDIT_LIST_SEARCH;
//        }
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        headersdatas.put("resourceId", employee.getFinanceResourceId());
//        //先准备一个List
//        Map<String, String> formbody = new HashMap<String, String>();
//        formbody.put("method", "0");
//        formbody.put("size", "10");
//        formbody.put("sort", "");
//        JsonArray formOIDs=new JsonArray();
//        JsonArray departmentOids=new JsonArray();
//        JsonObject body=new JsonObject();
//        body.addProperty("businessCode",businessCode);
//        body.add("formOIDs",formOIDs);
//        body.add("departmentOids",departmentOids);
//        body.addProperty("status","prending_audit");
//
//        String res = doPost(url,headersdatas,formbody,body.toString(),null,  employee);
//        responseEntity=new JsonParser().parse(res).getAsJsonArray();
//        return  responseEntity;
//    }

//    /**
//     * 报销单财务审核通过
//     * @param employee
//     * @param expenseReportOID
//     * @param approvalTxt
//     * @return
//     * @throws HttpStatusException
//
//     */
//    public  JsonObject expensereportAuditpass(Employee employee, String  expenseReportOID, int type,String approvalTxt) throws HttpStatusException {
//        JsonObject responseEntity=null;
//        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSEREPORT_AUDITPASS;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        JsonObject body=new JsonObject();
//        JsonObject expenseReport=new JsonObject();
//        JsonArray entities=new JsonArray();
//        expenseReport.addProperty("entityOID",expenseReportOID);
//        expenseReport.addProperty("entityType",type);
//        entities.add(expenseReport);
//        body.addProperty("generalLedgerDate",getUTCDate(0));
//        body.addProperty("approvalTxt",approvalTxt);
//        body.add("entities",entities);
//        String res= baseRequest.doPost(url,headersdatas,null,body.toString(),null,  employee);
//        responseEntity=new JsonParser().parse(res).getAsJsonObject();
//        return  responseEntity;
//    }


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


