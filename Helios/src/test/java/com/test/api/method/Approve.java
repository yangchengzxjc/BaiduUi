package com.test.api.method;

import com.google.gson.JsonArray;
import com.hand.api.ApproveApi;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;

/**
 * @Author peng.zhang
 * @Date 2020/8/5
 * @Version 1.0
 **/
public class Approve {

    private ApproveApi approveApi;

    public Approve(){
        approveApi =new ApproveApi();
    }

    /**
     * 查询单据
     * @param employee
     * @param businessCode
     */
    public JsonArray reportSearch(Employee employee, String businessCode) throws HttpStatusException {
        return approveApi.reportSearch(employee,businessCode);
    }

    /**
     * 单据审批
     * @param employee
     * @param reportOID
     * @param entityType
     * @throws HttpStatusException
     */
    public void approveal(Employee employee,String reportOID,int entityType) throws HttpStatusException {
        approveApi.ReportApproval(employee,reportOID,entityType);
    }

}
