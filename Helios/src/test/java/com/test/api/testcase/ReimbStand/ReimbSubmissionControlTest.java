package com.test.api.testcase.ReimbStand;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportComponent;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.Infra.SetOfBooksMethod.SetOfBooksDefine;
import com.test.api.method.ReimbSubmissionControl;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.util.HashMap;

@Slf4j
public class ReimbSubmissionControlTest extends BaseTest {
    private Employee employee;
    private ExpenseReport expenseReport;
    private ExpenseReportInvoice expenseReportInvoice;
    private ReimbSubmissionControl reimbSubmissionControl;
    private SetOfBooksDefine setOfBooksDefine;
    private ExpenseReportComponent expenseReportComponent;

    HashMap<String,String> getrulesOid;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
        reimbSubmissionControl =new ReimbSubmissionControl();
        expenseReportComponent =new ExpenseReportComponent();
        setOfBooksDefine=new SetOfBooksDefine();
    }

//    @BeforeMethod(description = "创建报销单提交管控规则")
    @Test
    public void creatRules() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        reimbSubmissionControl.addReimbSubmissionControl(employee,rules,"自动化测试-日常报销单",employee.getCompanyName());
    }

    @Test(priority = 1,description = "报销单提交管控规则校验")
    public void checkRules()throws HttpStatusException{
        //新建报销单
        FormComponent component=new FormComponent();
        InvoiceComponent invoiceComponent = new InvoiceComponent();
        component.setCompany(employee.getCompanyOID());
        component.setDepartment(employee.getDepartmentOID());
        component.setStartDate(UTCTime.getNowUtcTime());
        component.setEndDate(UTCTime.getUtcTime(3,0));
        //添加参与人员  参与人员的value 是一段json数组。
        JsonArray array = new JsonArray();
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"自动化测试-日常报销单"),"懿佳欢_stage"));
        array.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"自动化测试-日常报销单"),"17900001005"));
        log.info(array.toString());
        component.setParticipant(array.toString());
        component.setCause("报销单提交管控规则校验");
        log.info(String.valueOf(component));
        String expenseReportOID =expenseReport.createExpenseReport(employee,"自动化测试-日常报销单",component).get("expenseReportOID");
        log.info(expenseReportOID);
        //添加费用参与人员
        JsonArray data =new JsonArray();
        data.add(expenseReportComponent.getParticipant(employee,expenseReport.getFormOID(employee,"自动化测试-日常报销单"),"懿佳欢_stage"));
        invoiceComponent.setParticipants(data.toString());
        String cityCode =expenseReportComponent.getCityCode(employee,"西安市");
        invoiceComponent.setCity(cityCode);
        JsonObject startAndEndDate = new JsonObject();
        startAndEndDate.addProperty("startDate",UTCTime.getNowStartUtcDate());
        startAndEndDate.addProperty("endDate",UTCTime.getUTCDateEnd(2));
        startAndEndDate.addProperty("duration",2);
        invoiceComponent.setStartAndEndData(startAndEndDate.toString());
        String invoiceOID = expenseReportInvoice.createExpenseInvoice(employee,invoiceComponent,"自动化测试-报销标准",expenseReportOID,200.00,new JsonArray()).get("invoiceOID");
        log.info(invoiceOID);
        String time =UTCTime.getBeijingDay(0);
        String message ="费用消费日期不得超过报销单提交日期";
        log.info("需要断言的:{}",message);
        assert expenseReport.expenseReportSubmitCheck(employee,expenseReportOID).toString().contains(message);
        expenseReport.expenseReportSubmit(employee,expenseReportOID);
        expenseReport.withdraw(employee,expenseReportOID);
        expenseReport.removeInvoice(employee,expenseReportOID,invoiceOID);
        expenseReportInvoice.deleteInvoice(employee,invoiceOID);
        expenseReport.deleteExpenseReport(employee,expenseReportOID);
    }

//    @AfterMethod(description = "删除规则")
//    public void deleteRules() throws HttpStatusException{
//        //删除规则
//        reimbSubmissionControl.deleteReimbSubmissionRules(employee,getrulesOid.get("rulesOid"));
//
//    }
}