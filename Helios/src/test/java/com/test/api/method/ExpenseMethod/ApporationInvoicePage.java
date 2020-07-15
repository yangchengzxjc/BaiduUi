package com.test.api.method.ExpenseMethod;

import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author peng.zhang
 * @Date 2020/7/15
 * @Version 1.0
 **/
public class ApporationInvoicePage {

    private ExpenseReportInvoice expenseReportInvoice;
    private ExpenseReportComponent component;

    public ApporationInvoicePage(){
        expenseReportInvoice =new ExpenseReportInvoice();
        component = new ExpenseReportComponent();
    }

    /**
     * 获取默认的分摊行
     * @param employee
     * @param expenseReportOID
     * @param deptCode
     * @throws HttpStatusException
     */
    public JsonObject defaultApporationLine(Employee employee,String expenseReportOID,String deptCode,double proportion,double amount) throws HttpStatusException {
        //查询部门信息
        Map map =component.queryDepartment(employee,deptCode);
        String costCenterItemOID = map.get("departmentOID").toString();
        String departmentName = map.get("name").toString();
        return expenseReportInvoice.apportionLine(employee,true,"分摊费用类型",proportion,amount,"CNY",
                expenseReportOID,costCenterItemOID,departmentName,deptCode);
    }

    /**
     * 普通分摊行
     * @param employee
     * @param expenseReportOID
     * @param deptCode
     */
    public JsonObject ordinaryApporationLine(Employee employee,String expenseReportOID,String deptCode,double proportion,double amount) throws HttpStatusException {
        //查询部门信息
        Map map = component.queryDepartment(employee,deptCode);
        String costCenterItemOID = map.get("departmentOID").toString();
        String departmentName = map.get("name").toString();
        return expenseReportInvoice.apportionLine(employee,false,"分摊费用类型",proportion,amount,"CNY",
                expenseReportOID,costCenterItemOID,departmentName,deptCode);
    }
}
