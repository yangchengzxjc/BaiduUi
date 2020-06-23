package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.baseMethod.Base;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.BaseConstant;
import com.hand.basicObject.Employee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class ApplicationApi extends Base{
//    /**
//     * 构造方法
//     * @param Username
//     * @param Password
//     */
//    private BaseRequest baseRequest =new BaseRequest();
//    private EmployeeInformation information = new EmployeeInformation();
//
//    /**
//     * 获得我的申请单列表
//     * @param employee
//     * @param businessCode
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonArray getmyapplicationlists(Employee employee, String businessCode) throws HttpStatusException {
//        String url=employee.getEnvironment().getUrl()+ ApiPath.MY_APPLICATION_LIST;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        Map<String, String> datas = new HashMap<String, String>();
//        datas.put("withBudget", "false");
//        datas.put("withCustomForm", "false");
//        datas.put("withCustomFormProperty", "false");
//        datas.put("withCustomFormValue", "false");
//        datas.put("withParticipant", "false");
//        datas.put("withUserInfo", "false");
//        datas.put("status", "1001&status=1002&status=1003&status=1004&status=1005&status=1006&status=1007&status=1008");
//        datas.put("method", "0");
//        datas.put("size", "10");
//        datas.put("businessCode", businessCode);
//        String res= baseRequest.doGet(url,headersdatas,datas,employee);
//        return new JsonParser().parse(res).getAsJsonArray();
//    }
//
//    /**
//     * 获得申请单详情
//     * @param employee
//     * @param applicationOID
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonObject getapplicationDetal(Employee employee, String applicationOID) throws  HttpStatusException {
//        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.APPLICATION_DETAL,applicationOID);
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        String res= baseRequest.doGet(url,headersdatas,null,employee);
//        return new JsonParser().parse(res).getAsJsonObject();
//    }
//
//    /**
//     * 核销查询我的借款列表
//     * @param employee
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonArray getLoanApplication(Employee employee) throws  HttpStatusException {
//        String url=employee.getEnvironment().getUrl()+ ApiPath.APPLICATION_LAN_MY;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        Map<String, String> formbody = new HashMap<String, String>();
//        formbody.put("applicantOID", employee.getUserOID());
//        formbody.put("currencyCode", BaseConstant.CURRENCY);
//        formbody.put("companyOID", employee.getCompanyOID());
//        formbody.put("method", "0");
//        formbody.put("size", "100");
//        formbody.put("status", "1005&status=1006");
//
//        String res= baseRequest.doGet(url,headersdatas,formbody,employee);
//        return new JsonParser().parse(res).getAsJsonArray();
//    }
//
//
//    /**
//     * 申请单提交
//     * @param employee
//     * @param ExpenseReportdetal
//     * @return
//     * @throws HttpStatusException
//     */
//    public  JsonObject applicationSubmit (Employee employee, JsonObject  ExpenseReportdetal) throws HttpStatusException {
//        JsonObject responseEntity=null;
//        String url=employee.getEnvironment().getUrl()+ ApiPath.APPLICATION_SUBMIT;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        String res= baseRequest.doPost(url,headersdatas,null,ExpenseReportdetal.toString(),null,  employee);
//        responseEntity=new JsonParser().parse(res).getAsJsonObject();
//
//        return  responseEntity;
//    }
//
//
//    /**
//     * 申请单审批通过
//     * @param employee
//     * @param expenseReportOID
//     * @param approvalTxt
//     * @return
//     * @throws HttpStatusException
//     */
//    public  JsonObject application_approval(Employee employee, String  expenseReportOID,String approvalTxt) throws HttpStatusException {
//        JsonObject responseEntity=null;
//        String url=employee.getEnvironment().getUrl()+ ApiPath.EXPENSEREPORT_APPROVAL;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//
//        JsonObject body=new JsonObject();
//        JsonArray entities=new JsonArray();
//        JsonObject entitie =new JsonObject();
//        entitie.addProperty("entityOID",expenseReportOID);
//        entitie.addProperty("approverOID",employee.getUserOID());
//        entitie.addProperty("entityType",BaseConstant.ENTITY_TYPE[0]);
//        entities.add(entitie);
//
//        body.add("entities",entities);
//        body.addProperty("approvalTxt",approvalTxt);
//        String res= baseRequest.doPost(url,headersdatas,null,body.toString(),null,  employee);
//        responseEntity=new JsonParser().parse(res).getAsJsonObject();
//
//        return  responseEntity;
//    }
//
//
//
//    /**
//     * 员工删除申请单
//     * @param employee
//     * @param applicationOID
//     * @return
//     * @throws HttpStatusException
//     */
//    public  String applicationDelete (Employee employee,  String  applicationOID) throws HttpStatusException {
//        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.APPLICATION_DELETE,applicationOID);
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        JsonObject body=new JsonObject();
//        String res= baseRequest.doDlete(url,headersdatas,null,body, employee);
//        return  res;
//    }
//
//    /**
//     * 查询表单默认值
//     * @param employee
//     * @param formOID
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonArray getFormDefault_values(Employee employee,String formOID) throws  HttpStatusException {
//        String url=employee.getEnvironment().getUrl()+ ApiPath.DEFAULT_VALUES;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        Map<String, String> urlform = new HashMap<String, String>();
//        urlform.put("formOID",formOID);
//        urlform.put("userOID",employee.getUserOID());
//        String res= baseRequest.doGet(url,headersdatas,urlform,employee);
//        return new JsonParser().parse(res).getAsJsonArray();
//    }
//
//    /**
//     * 新建申请单，所有的参数值采取查的形式
//     * @param employee
//     * @param formdetal
//     * @param Cause
//     * @param Cause
//     * @return DepartmentOID 部门OID
//     * @throws HttpStatusException
//     */
//    public  JsonObject newApplication(Employee employee, JsonObject  formdetal, String Cause,String DepartmentOID,JsonArray attachments) throws HttpStatusException {
//        JsonObject responseEntity=null;
//        String url=employee.getEnvironment().getUrl()+ ApiPath.APPLICATION_DRAFT;
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        JsonArray customFormFields=formdetal.get("customFormFields").getAsJsonArray();
//        JsonArray custFormValues=formdetal.get("customFormFields").getAsJsonArray();
//        for (int i=0;i<custFormValues.size();i++)
//        {
//            JsonObject data= custFormValues.get(i).getAsJsonObject();
//            if (data.get("messageKey").getAsString().equals("select_department"))
//                data.addProperty("value",DepartmentOID);
//            if (data.get("messageKey").getAsString().equals("title"))
//                data.addProperty("value",Cause);
//            if (data.get("messageKey").getAsString().equals("common.date"))
//                data.addProperty("value",getUTCDate(0));
//            if (data.get("messageKey").getAsString().equals("date"))
//                data.addProperty("value",getUTCDate(0));
//            if (data.get("messageKey").getAsString().equals("employee_expand"))
//                data.addProperty("value","888");
//            if (data.get("messageKey").getAsString().equals("total_budget"))
//                data.addProperty("value",9999999999L);
//            if (data.get("messageKey").getAsString().equals("cust_list"))
//            {
//                String customEnumerationOID=new JsonParser().parse(data.get("dataSource").getAsString()).getAsJsonObject().get("customEnumerationOID").getAsString();
//                JsonArray customenumerationlist = getCustomenumerationoid(employee,customEnumerationOID);
//                data.addProperty("value", customenumerationlist.get(0).getAsJsonObject().get("value").getAsString());
//            }
//            if (data.get("messageKey").getAsString().equals("attachment"))
//                if (!attachments.isJsonNull())
//                    data.addProperty("value", attachments.toString());
//        }
//
//        formdetal.remove("custFormValues");
//        formdetal.add("custFormValues",custFormValues);
//        formdetal.add("customFormFields",customFormFields);
//        formdetal.addProperty("currencySame",false);
//        formdetal.addProperty("applicantOID",employee.getUserOID());
////        log.info("报销单保存报文:"+formdetal.toString());
//
//        String res= baseRequest.doPost(url,headersdatas,null,formdetal.toString(),null,employee);
//
//        responseEntity=new JsonParser().parse(res).getAsJsonObject();
//
//        return  responseEntity;
//    }
//
//    /**
//     * 获取值列表信息
//     * @param employee
//     * @return
//     * @throws HttpStatusException
//     */
//    public JsonArray getCustomenumerationoid( Employee employee,String customEnumerationOID) throws  HttpStatusException {
//        String url=employee.getEnvironment().getUrl()+ String.format(ApiPath.GET_CUSTOMENUMERATIONOID,customEnumerationOID);
//        Map<String, String> headersdatas = new HashMap<String, String>();
//        headersdatas.put("Authorization", "Bearer "+employee.getAccessToken()+"");
//        String res= baseRequest.doGet(url,headersdatas,null,employee);
//        return new JsonParser().parse(res).getAsJsonArray();
//    }
}


