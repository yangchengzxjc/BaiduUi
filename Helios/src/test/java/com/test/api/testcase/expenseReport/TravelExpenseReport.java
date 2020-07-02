package com.test.api.testcase.expenseReport;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * @Author peng.zhang
 * @Date 2020/6/29
 * @Version 1.0
 **/
public class TravelExpenseReport extends BaseTest {


    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private Employee employee;
    private FormComponent component;
    private InvoiceComponent invoiceComponent;
    private ExpenseReportComponent expenseReportComponent;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("hly12345") String pwd, @Optional("stage") String env){
        expenseReport = new ExpenseReport();
        component = FormComponent.builder().cause("差旅报销单关联单申请单").build();
        invoiceComponent = new InvoiceComponent();
        expenseReportInvoice = new ExpenseReportInvoice();
        expenseReportComponent =new ExpenseReportComponent();
        employee=getEmployee(phoneNumber,pwd,env);
    }

    @Test(description = "差旅报销单关联单个申请单")
    public void createTraveExpenseReportTest1() throws HttpStatusException {
        //表单初始化
        component.setDepartment(employee.getDepartmentOID());
        //关联申请单
        String applicationOID = expenseReport.getApplication(employee,"差旅报销单-节假日").get(0);
        component.setApplicationOID(applicationOID);
        //  参与人
        ArrayList<String> applicationOIDs =new ArrayList<>();
        applicationOIDs.add(applicationOID);
        component.setParticipant(expenseReport.getValueFromApplication(employee,applicationOIDs,"参与人员"));
        String expenseReportOID = expenseReport.createTravelExpenseReport(employee,false,"差旅报销单-节假日",component).get("expenseReportOID");
        //报销单新建费用
        expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,expenseReportOID,"交通",23.43);
        expenseReport.expenseReportSubmit(employee,expenseReportOID);
    }

    @Test(description = "差旅报销单关联多个申请单(两个)")
    public void createTravelExpenseReportTest2() throws HttpStatusException {
        //表单初始化部门
        component.setDepartment(employee.getDepartmentOID());
        //关联申请单 给
        String applicationOID1 = expenseReport.getApplication(employee,"差旅报销单-节假日").get(0);
        String applicationOID2 = expenseReport.getApplication(employee,"差旅报销单-节假日").get(1);
        component.setApplicationOID(applicationOID1);
        JsonArray array = new JsonArray();
        JsonObject object1 =new JsonObject();
        JsonObject object2 =new JsonObject();
        object1.addProperty("applicationOID",applicationOID1);
        object1.addProperty("tenantId",employee.getTenantId());
        object2.addProperty("applicationOID",applicationOID2);
        object2.addProperty("tenantId",employee.getTenantId());
        array.add(object1);
        array.add(object2);
        component.setExpenseReportApplicationDTOS(array);
        //  参与人
        ArrayList<String> applicationOIDs =new ArrayList<>();
        applicationOIDs.add(applicationOID1);
        applicationOIDs.add(applicationOID2);
        component.setParticipant(expenseReport.getValueFromApplication(employee,applicationOIDs,"参与人员"));
        String expenseReportOID = expenseReport.createTravelExpenseReport(employee,true,"差旅报销单-节假日",component).get("expenseReportOID");
        //报销单新建费用
        expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"交通",expenseReportOID,23.43);
        expenseReport.expenseReportSubmit(employee,expenseReportOID);
    }
}
