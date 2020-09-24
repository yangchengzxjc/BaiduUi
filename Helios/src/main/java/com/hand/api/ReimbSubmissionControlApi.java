package com.hand.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicconstant.ApiPath;
import com.hand.basicconstant.ResourceId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.ml.distance.EarthMoversDistance;

import java.util.HashMap;

@Slf4j
public class ReimbSubmissionControlApi extends BaseRequest{

    /**
     * 新建报销单提交管控规则
     * @param employee
     * @param name
     * @param controlLevel 控制方式 WARN/FORBID
     * @param forms
     * @param message
     * @param levelCode
     * @param levelOrgId
     * @param levelOrgName
     * @param companys
     * @return
     * @throws HttpStatusException
     */
    public String creatRules (Employee employee,String name,String controlLevel,JsonArray forms,String message,String levelCode,
                              String levelOrgId,String levelOrgName,JsonArray companys)throws HttpStatusException {
        String url = employee.getEnvironment().getUrl()+ ApiPath.ADD_REIMB_STANDARD;
        HashMap<String, String> mapParams3 = new HashMap<>();
        mapParams3.put("roleType","TENANT");
        JsonObject body = new JsonObject();
        body.addProperty("status",true);
        body.addProperty("name",name);
        body.add("forms",forms);
        body.addProperty("controlLevel",controlLevel);
        body.addProperty("message",message);
        //多语言
        JsonObject object =new JsonObject();

        JsonArray nameList= new JsonArray();
        JsonObject name1 =new JsonObject();
        name1.addProperty("language","en");
        name1.addProperty("value",name);
        JsonObject name2 =new JsonObject();
        name2.addProperty("language","zh_cn");
        name2.addProperty("value",name);
        JsonObject name3 =new JsonObject();
        name3.addProperty("language","zh_TW");
        name3.addProperty("value",name);
        nameList.add(name1);
        nameList.add(name2);
        nameList.add(name3);
        object.add("name",nameList);

        JsonArray nameList1= new JsonArray();
        JsonObject message1 =new JsonObject();
        message1.addProperty("language","en");
        message1.addProperty("value",message);
        JsonObject message2 =new JsonObject();
        message2.addProperty("language","zh_cn");
        message2.addProperty("value",message);
        JsonObject message3 =new JsonObject();
        message3.addProperty("language","zh_TW");
        message3.addProperty("value",message);
        nameList1.add(message1);
        nameList1.add(message2);
        nameList1.add(message3);
        object.add("message",nameList1);

        body.add("i18n",object);
        body.addProperty("businessType",1002);
        body.addProperty("levelCode",levelCode);
        body.addProperty("levelOrgId",levelOrgId);
        body.addProperty("levelOrgName",levelOrgName);
        body.add("companys",companys);
        String res = doPost(url,getHeader(employee.getAccessToken(),"reimbursement-standard", ResourceId.INFRA),mapParams3,body.toString(),null,employee);
        return new JsonParser().parse(res).getAsString();
    }

    /**
     * 获取规则默认详情
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public String getRules(Employee employee, String rulesOid)throws HttpStatusException{
        String url = employee.getEnvironment().getUrl() + String.format(ApiPath.GET_SUBMISSION_RULES,rulesOid) ;
        HashMap<String,String> mapParams = new HashMap<>();
        mapParams.put("roleType","TENANT");
        String res=doGet(url,getHeader(employee.getAccessToken(),"reimb-submission-control",ResourceId.INFRA),mapParams,employee);
        return res;
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
        String res = doPost(url,getHeader(employee.getAccessToken(),"reimb-submission-control", ResourceId.INFRA),mapParams,body.toString(),null,employee);
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
        String res=doGet(url,getHeader(employee.getAccessToken(),"reimb-submission-control",ResourceId.INFRA),mapParams,employee);
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
        doPost(url,getHeader(employee.getAccessToken(),"reimb-submission-control",ResourceId.INFRA),mapParams,new JsonObject().toString(),null,employee);
    }
}
