package com.test.api.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.api.ReimbSubmissionControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;

public class ReimbSubmissionControl {
    private ReimbSubmissionControlApi reimbSubmissionControl;

    public ReimbSubmissionControl(){
        reimbSubmissionControl = new ReimbSubmissionControlApi();
    }

    /**
     * 报销单提交管控规则创建
     * @param employee
     * @param name
     * @param controlLevel
     * @param forms
     * @param message
     * @param levelCode
     * @param levelOrgId
     * @param levelOrgName
     * @param companys
     * @return
     * @throws HttpStatusException
     */
    public String addReimbSubmissionControl(Employee employee,String name,String controlLevel, JsonArray forms,String message,String levelCode,
                                            String levelOrgId,String levelOrgName,JsonArray companys)throws HttpStatusException {
        String rulesOid =reimbSubmissionControl.creatRules(employee,name,controlLevel,forms,message,levelCode,levelOrgId,
                levelOrgName,companys);
        return rulesOid;
    }

    /**
     * 获取规则默认详情
     * @param employee
     * @param rulesOid
     * @return
     * @throws HttpStatusException
     */
    public String  getRules(Employee employee, String rulesOid)throws HttpStatusException{
//        JsonObject getRulesDetails =new JsonObject();
        String getRulesDetails=reimbSubmissionControl.getRules(employee,rulesOid);
        return getRulesDetails;
    }
}
