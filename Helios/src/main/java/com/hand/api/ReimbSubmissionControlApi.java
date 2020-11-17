package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
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
        String res = doPost(url,getHeader(employee.getAccessToken(), HeaderKey.REIMB_STANDARD, ResourceId.SUBMIT_CONTROL),mapParams,ruleObject.toString(),null,employee);
        return new JsonParser().parse(res).getAsString();
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
     * @param controlItem 管控项 1002 报销单提交日期；
     * @param valueType 取值方式 1004 <;
     * @param controlCond 条件 1002 费用消费日期；1004 关联申请单的结束日期
     * @param mixedItem 条件中的符号 1001 +；1002 -；
     * @param extendValue 具体数值
     * @return
     * @throws HttpStatusException
     */
    public String addRulesItems(Employee employee,String rulesOid,int controlItem,int valueType,
                                int controlCond,int mixedItem,int extendValue)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.ADD_SUBMISSION_ITEM,rulesOid);
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        JsonObject body =new JsonObject();
        body.addProperty("controlItem",controlItem);
        body.addProperty("valueType",valueType);
        body.addProperty("controlCond",controlCond);
        body.addProperty("mixedItem",mixedItem);
        body.addProperty("extendValue",extendValue);
        String res = doPost(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL, ResourceId.SUBMIT_CONTROL),mapParams,body.toString(),null,employee);
        return res;
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
    public JsonArray  controlGetForm(Employee employee,String setOfBooksId,String keyWord) throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ String.format(ApiPath.QUERY_FORM,keyWord,keyWord,setOfBooksId);
        String response = doPost(url,getHeader(employee.getAccessToken(),HeaderKey.REIMB_SUBMIT_CONTROL,ResourceId.SUBMIT_CONTROL),null,new JsonObject().toString(),null,employee);
        return new JsonParser().parse(response).getAsJsonArray();
    }
}
