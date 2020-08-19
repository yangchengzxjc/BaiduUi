package com.test.api.testcase.expenseReport.TravelSubsidy;

import com.google.gson.JsonArray;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/8/3
 * @Version 1.0
 **/
public class TravelSubsidyTest extends BaseTest {

    private Employee employee;
    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private FormComponent component;
    private ExpenseReportComponent expenseReportComponent;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void init(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        employee =getEmployee(phoneNumber,pwd,env);
        expenseReport =new ExpenseReport();
        component =new FormComponent();
    }

    @Test
    public void travelSubsidyTest1() throws HttpStatusException {
        //申请单初始化部门
        component.setDepartment(employee.getDepartmentOID());
        //初始化开始结束日期
        component.setStartDate(UTCTime.getNowStartUtcDate());
        component.setEndDate(UTCTime.getUTCDateEnd(5));
        component.setCause("自动化测试差补");
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"测试差旅申请单1"),"懿佳欢_stage"));
        component.setParticipant(array.toString());
        //关联申请单
        String applicationOID = expenseReport.getApplication(employee,"测试差旅申请单1").get(0);
    }
}
