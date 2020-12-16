package com.test.api.method;

import com.google.gson.JsonArray;
import com.hand.api.ApproveApi;
import com.hand.api.FinanceApi;
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
     * 单据审批 -审批人是自己
     * @param employee
     * @param reportOID
     * @param entityType 表示单据类型   1001：申请单    1002：表示报销单
     * @throws HttpStatusException
     */
    public int approveal(Employee employee,String reportOID,int entityType) throws HttpStatusException {
        return approveApi.reportApproval(employee,employee.getUserOID(),reportOID,entityType).get("successNum").getAsInt();
    }

    /**
     * 单据审批 -审批人是任何人
     * @param employee
     * @param reportOID
     * @param entityType 表示单据类型   1001：申请单    1002：表示报销单
     * @throws HttpStatusException
     */
    public int approveal(Employee employee,String approveOID, String reportOID,int entityType) throws HttpStatusException {
        return approveApi.reportApproval(employee,approveOID,reportOID,entityType).get("successNum").getAsInt();
    }

    /**
     * 财务驳回单据
     * @param employee
     * @param expenseReportOID
     * @param entityType
     */
    public int auditReject(Employee employee,String expenseReportOID,int entityType) throws HttpStatusException {
        FinanceApi financeApi = new FinanceApi();
        return financeApi.reportAuditreject(employee,expenseReportOID,entityType).get("failNum").getAsInt();
    }
}
