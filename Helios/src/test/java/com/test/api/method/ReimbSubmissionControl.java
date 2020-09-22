package com.test.api.method;

import com.google.gson.JsonArray;
import com.hand.api.ReimbSubmissionControlApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;

public class ReimbSubmissionControl {
    private ReimbSubmissionControlApi reimbSubmissionControl;

    public ReimbSubmissionControl(){
        reimbSubmissionControl = new ReimbSubmissionControlApi();
    }

    public String addReimbSubmissionControl(Employee employee,String name,String controlLevel, JsonArray forms,String message,String levelCode,
                                            String levelOrgId,String levelOrgName,JsonArray companys)throws HttpStatusException {
        String rulesOid =reimbSubmissionControl.creatRules(employee,name,controlLevel,forms,message,levelCode,levelOrgId,
                levelOrgName,companys);
        return rulesOid;

    }

}
