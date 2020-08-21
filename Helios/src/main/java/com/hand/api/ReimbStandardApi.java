package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.ResourceId;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class ReimbStandardApi  extends BaseRequest {

    /*
    *获取所有账套
    * @ param employee
    * @ return
    * @ throws HttpStatusException
     */
    public JsonArray getSetOfBooks (Employee employee) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.GETSET_OF_BOOKS;
        HashMap<String, String> mapParams1 = new HashMap<>();
        mapParams1.put("roleType", "TENANT");
        mapParams1.put("enabled", "true");
        mapParams1.put("page", "0");
        mapParams1.put("size", "9999");
        mapParams1.put("page", "0");
        mapParams1.put("size", "10");
        String res =doGet(url,getHeader(employee.getAccessToken(),"reimbursement-standard"),mapParams1,employee);
        return new JsonParser().parse(res).getAsJsonArray();
//        "/api/setOfBooks/query/dto?enabled=true&roleType=TENANT&page=0&size=9999&page=0&size=10";
    }
    /*
     * 获取启用公司
     * @ param employee
     * @ param setOfBookId 账套id
     * @ return
     * @ throws HttpStatusException
     */
    public JsonArray getEnabledCompany (Employee employee, String setOfBooksId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.ENABLED_COMPANY;
        HashMap<String, String> mapParams1 = new HashMap<>();
        mapParams1.put("roleType", "TENANT");
        mapParams1.put("enabled", "true");
        mapParams1.put("page", "0");
        mapParams1.put("size", "10");
        mapParams1.put("setOfBooksId", setOfBooksId);
        String res = doGet(url, getHeader(employee.getAccessToken()), mapParams1, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }
    /*
     * 获取账套件级人员组
     * @ param employee
     * @ param setOfBookId 账套id
     * @ return
     * @ throws HttpStatusException
     */
    public JsonArray getUserGroups (Employee employee,String setOfBooksId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.USER_GROUPS;
        HashMap<String, String> mapParams2 = new HashMap<>();
        mapParams2.put("roleType", "TENANT");
        mapParams2.put("enabled", "true");
        mapParams2.put("page", "0");
        mapParams2.put("size", "10");
        mapParams2.put("levelCode","SET_OF_BOOK");
        mapParams2.put("levelOrgId", setOfBooksId);
        String res = doGet(url, getHeader(employee.getAccessToken(),"reimbursement-standard"), mapParams2, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }
    /*
     * 获取费用类型
     * @ param employee
     * @ param setOfBookId 账套id
     * @ return
     * @ throws HttpStatusException
     */
    public JsonArray getExpenseType (Employee employee,String setOfBooksId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.EXPENSE_TYPE;
        HashMap<String, String> mapParams3 = new HashMap<>();
        mapParams3.put("roleType", "TENANT");
        mapParams3.put("page", "0");
        mapParams3.put("size", "10");
        mapParams3.put("enabled", "true");
        mapParams3.put("subsidyType", "0");
        mapParams3.put("setOfBooksId", setOfBooksId);
        String res = doGet(url, getHeader(employee.getAccessToken(),"reimbursement-standard"), mapParams3, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }
    /*
     * 获取单据类型
     * @ param employee
     * @ param setOfBookId 账套id
     * @ return
     * @ throws HttpStatusException
     */
    public JsonArray getFormTpye (Employee employee,String setOfBooksId) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.FORM_TYPE;
        HashMap<String, String> mapParams3 = new HashMap<>();
        mapParams3.put("roleType", "TENANT");
        mapParams3.put("page", "0");
        mapParams3.put("size", "10");
        mapParams3.put("formTypeList", "3001");
        mapParams3.put("formTypeList", "3002");
        mapParams3.put("formTypeList", "3003");
        mapParams3.put("setOfBooksId", setOfBooksId);
        String res = doGet(url, getHeader(employee.getAccessToken()), mapParams3, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }
    private ReimbStandardApi reimbStandardApi;
    private Object JsonObject;
    /*
     * 新建报销标准规则
     * @ param employee
     * @ name 规则名称
     * @ controlLevel 控制力度 FORBID、WARN
     * @ levelCode 层级 COMPANY、SET_OF_BOOK
     * @ controlType 控制方式 DAY（MOUTH、QUARTER、YEAR）、SUMMARY、SINGLE
     * @ controlModeType 控制方式类型 PERIOD SUMMARY
     * @ message 提示内容
     * @ userGroups 人员组
     * @ expenseTypes 费用类型
     * @ forms 单据类型
     * @ companys 适用公司
     * @ businessType 1001 报销标准 1002 提交管控
     */

    public String creatReimbStandardRules (Employee employee,String name, String controlLevel,
                                               String levelCode,String levelOrgId,String controlType,String controlModeType,
                                               String message,
                                               JsonArray userGroups, JsonArray expenseTypes,JsonArray forms,
                                               JsonArray companys) throws HttpStatusException{
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_Reimb_Standard;
        HashMap<String, String> mapParams3 = new HashMap<>();
        mapParams3.put("roleType", "TENANT");
        JsonObject body= new JsonObject();
        body.addProperty("status",true);
        body.add("userGroups",userGroups);//人员组
        body.addProperty("controlModeType","SINGLE");
        body.addProperty("name",name);
        body.add("expenseTypes",expenseTypes);
        body.add("forms",forms);
        body.addProperty("crossCompanyStandard","OWNER");
        body.addProperty("controlLevel",controlLevel);
        body.addProperty("complianceCheck",false);
        body.addProperty("message",message);
        body.addProperty("participantsEnable",false);
        //多语言
        JsonObject object =new JsonObject();

        JsonArray nameList= new JsonArray();
        JsonObject name1 =new JsonObject();
        name1.addProperty("language","en");
        name1.addProperty("value",name);
        JsonObject name2 =new JsonObject();
        name2.addProperty("language","zh_cn");
        name2.addProperty("value",name);
        nameList.add(name1);
        nameList.add(name2);
        object.add("name",nameList);

        JsonArray nameList1= new JsonArray();
        JsonObject message1 =new JsonObject();
        message1.addProperty("language","en");
        message1.addProperty("value",message);
        JsonObject message2 =new JsonObject();
        message2.addProperty("language","en");
        message2.addProperty("value",message);
        nameList1.add(message1);
        nameList1.add(message2);
        object.add("message",nameList1);

        body.add("i18n",object);
        body.addProperty("type","AMOUNT");
        body.addProperty("businessType",1001);
        body.addProperty("levelCode",levelCode);
        body.addProperty("levelOrgId",levelOrgId);
        body.add("companys",companys);
        body.addProperty("controlType",controlType);
        body.addProperty("controlModeType",controlModeType);
        String res = doPost(url,getHeader(employee.getAccessToken(),"reimbursement-standard", ResourceId.INFRA),mapParams3,body.toString(),null,employee);
//        return new JsonParser().parse(res).getAsJsonObject();
        return res;
    }
    /**
     * 删除报销标准规则
     *
     */


}