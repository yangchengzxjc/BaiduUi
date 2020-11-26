package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.ResourceId;
import com.hand.utils.GsonUtil;
import com.hand.utils.RandomNumber;
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
        String url = employee.getEnvironment().getUrl()+ ApiPath.GET_SET_OF_BOOKS;
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
    public JsonArray getFormTpye (Employee employee,String setOfBooksId,String formName) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.FORM_TYPE,setOfBooksId,formName,formName);
        String res = doGet(url, getHeader(employee.getAccessToken(),"reimbursement-standard"), null, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }


    /**
     * 新增报销标准规则
     * @param employee
     * @param rules
     * @return
     * @throws HttpStatusException
     */
    public String addReimbStandardRules (Employee employee, StandardRules rules) throws HttpStatusException{
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_REIMB_STANDARD;
        HashMap<String, String> mapParams3 = new HashMap<>();
        mapParams3.put("roleType", "TENANT");
        String ruleString = GsonUtil.objectToString(rules);
        JsonObject ruleObject = new JsonParser().parse(ruleString).getAsJsonObject();
        //设置多语言字段
        JsonObject i18n =new JsonObject();
        i18n.add("name",GsonUtil.setLanguage(rules.getName()));
        i18n.add("message",GsonUtil.setLanguage(rules.getMessage()));
        ruleObject.add("i18n",i18n);
        String res = doPost(url,getHeader(employee.getAccessToken(),"reimbursement-standard", ResourceId.INFRA),mapParams3,ruleObject.toString(),null,employee);
        return new JsonParser().parse(res).getAsString();
    }

    /**
     * 删除规则
     * @param employee
     * @param rulesOid
     * @throws HttpStatusException
     */
    public void deleteReimbStandardRules(Employee employee, String rulesOid) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.DELETE_REIMB_STANDARD,rulesOid);
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("roleType", "TENANT");
       doPost(url,getHeader(employee.getAccessToken(),"reimbursement-standard",ResourceId.INFRA),mapParams,new JsonObject().toString(),null,employee);
    }

    /**
     * 获取报销标准规则的管控信息
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getControlItem(Employee employee,String rulesOid)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GET_CONTROLITEM,rulesOid);
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("roleType", "TENANT");
        String res=doGet(url,getHeader(employee.getAccessToken(),"reimbursement-standard",ResourceId.INFRA),mapParams,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 获取报销标准规则的基本标准
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getItem(Employee employee,String rulesOid)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GET_ITEM,rulesOid) ;
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENENT");
        String res=doGet(url,getHeader(employee.getAccessToken(),"reimbursement-standard",ResourceId.INFRA),mapParams,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 添加基本标准
     * @param employee
     * @param standardOid
     * @param amount
     * @param userGroups
     * @param citys
     * @return
     * @throws HttpStatusException
     */
    public String addItems(Employee employee,String standardOid,String rulesOid,Integer amount,JsonArray userGroups,JsonArray citys)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() +ApiPath.ADD_ITEM;
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENENT");
//        JsonArray body = new JsonArray();
        JsonObject body1 = new JsonObject();
//        body.add(body1);
        body1.addProperty("ruleOID",rulesOid);
        body1.addProperty("standardOID",standardOid);
        body1.addProperty("currencyCode","CNY");
        body1.addProperty("amount",amount);
        body1.add("userGroups",userGroups);
        body1.add("citys",citys);
        //1 是包含；0 是不包含
        body1.addProperty("cityAssociateType",1);
        body1.addProperty("userAssociateType",1);
        String res = doPost(url,getHeader(employee.getAccessToken(),"reimbursement-standard",ResourceId.INFRA),mapParams,body1.toString(),null,employee);
        return res;
    }

    /**
     * 查询报销标准规则
     * @param employee
     * @param ruleName
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getRules(Employee employee,String ruleName)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() +ApiPath.QUERY_REIM_RULES;
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        mapParams.put("page","0");
        mapParams.put("size","20");
        mapParams.put("distLevelOrgIds","");
        mapParams.put("expenseTypeIds","");
        mapParams.put("businessType","1001");
        mapParams.put("name",ruleName);
        String res = doGet(url, getHeader(employee.getAccessToken(),"reimbursement-standard",ResourceId.INFRA), mapParams, employee);
        return new JsonParser().parse(res).getAsJsonArray();

    }

    /**
     * 新增城市组
     * @param employee
     * @param cityGroupName
     * @param levelCode   组户级"TENANT"还是公司级"COMPANY"
     * @return
     * @throws HttpStatusException
     */
    public JsonObject addCityGroup(Employee employee,String cityGroupName,String levelCode) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.CREATE_CITY_GROUP;
        JsonObject body = new JsonObject();
        body.addProperty("levelName",cityGroupName);
        body.addProperty("comment",cityGroupName+"城市组");
        body.addProperty("code", RandomNumber.getUUID(6));
        body.addProperty("levelCode",levelCode);
        if(levelCode.equals("TENANT")){
            body.addProperty("levelOrgName",employee.getTenantName());
            body.addProperty("levelOrgId",employee.getTenantId());
        }else{
            body.addProperty("levelOrgName",employee.getCompanyName());
            body.addProperty("levelOrgId",employee.getCompanyId());
        }
        JsonObject i18n =new JsonObject();
        i18n.add("levelName", GsonUtil.setLanguage(cityGroupName));
        i18n.add("comment",GsonUtil.setLanguage(cityGroupName+"城市组"));
        body.add("i18n",i18n);
        String res = doPost(url,getHeader(employee.getAccessToken(),ResourceId.CITY_GROUP),null,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsJsonObject();
    }
}