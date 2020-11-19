package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.SubmitRuleItem;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.HeaderKey;
import com.hand.basicconstant.ResourceId;
import com.hand.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class ReimbSubmissionControlApi extends BaseRequest{

    /**
     * 新建报销单提交管控规则
     * @param employee
     * @return
     * @throws HttpStatusException
     */
   public String creatSubmitRules (Employee employee, SubmitRules rules)throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_REIMB_STANDARD;
        String ruleString = GsonUtil.objectToString(rules);
        JsonObject ruleObject = new JsonParser().parse(ruleString).getAsJsonObject();
        //设置多语言字段
        JsonObject i18n =new JsonObject();
        i18n.add("name",GsonUtil.setLanguage(rules.getName()));
        i18n.add("message",GsonUtil.setLanguage(rules.getMessage()));
        ruleObject.add("i18n",i18n);
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        return doPost(url,getHeader(employee.getAccessToken(), HeaderKey.REIMB_STANDARD, ResourceId.SUBMIT_CONTROL),mapParams,ruleObject.toString(),null,employee);

    }

    /**
     * 获取规则默认详情
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonObject getRules(Employee employee, String rulesOid)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GET_SUBMISSION_RULES,rulesOid) ;
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        String response = doGet(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL,ResourceId.SUBMIT_CONTROL),mapParams,employee);
        return new JsonParser().parse(response).getAsJsonObject();
    }

    /**
     * 新增管控项
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public String addRulesItems(Employee employee, String rulesOid, SubmitRuleItem item)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.ADD_SUBMISSION_ITEM,rulesOid);
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        String ruleString = GsonUtil.objectToString(item);
        JsonObject itemObject = new JsonParser().parse(ruleString).getAsJsonObject();
        return doPost(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL, ResourceId.SUBMIT_CONTROL),mapParams,itemObject.toString(),null,employee);
    }

    /**
     * 获取管控项
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public JsonArray getItems(Employee employee,String rulesOid)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GET_SUBMISSION_ITEM,rulesOid);
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        String res = doGet(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL,ResourceId.SUBMIT_CONTROL),mapParams,employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }

    /**
     * 规则删除
     * @param employee
     * @param rulesOid
     * @throws HttpStatusException
     */
    public void deleteReimbSubmissionControlRules(Employee employee,String rulesOid) throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.DELETE_REIMB_STANDARD,rulesOid);
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        doPost(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL,ResourceId.SUBMIT_CONTROL),mapParams,new JsonObject().toString(),null,employee);
    }

    /**
     * 查询报销单提交管控的表单
     * @param employee
     * @param setOfBooksId  账套id
     * @param keyWord  表单名称
     * @return
     * @throws HttpStatusException
     */
    public JsonArray  controlGetForm(Employee employee,String setOfBooksId,String companyOID,String keyWord) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ String.format(ApiPath.QUERY_FORM,keyWord,keyWord,setOfBooksId,companyOID);
        String response = doGet(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL,ResourceId.SUBMIT_CONTROL),null,employee);
        return new JsonParser().parse(response).getAsJsonArray();
    }
    
    /*
     * 获取费用类型
     * @ param employee
     * @ param setOfBookId 账套id
     * @ return
     * @ throws HttpStatusException
     */
    public JsonArray getExpenseType (Employee employee,String setOfBooksId,String expenseName) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl() + ApiPath.EXPENSE_TYPE;
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("roleType", "TENANT");
        mapParams.put("page", "0");
        mapParams.put("size", "10");
        mapParams.put("name",expenseName);
        mapParams.put("nameLable",expenseName);
        mapParams.put("levelCode","ALL");
        mapParams.put("enabled", "true");
        if(setOfBooksId.length()>10){
            mapParams.put("setOfBooksId", setOfBooksId);
            mapParams.put("companyId","");
        }else{
            mapParams.put("companyId",setOfBooksId);
            mapParams.put("setOfBooksId","");
        }
        String res = doGet(url, getHeader(employee.getAccessToken(),"reimbursement-standard"), mapParams, employee);
        return new JsonParser().parse(res).getAsJsonArray();
    }



}
