package com.test.api.testcase.expenseControl.SubmissionControl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.FormComponent;
import com.hand.basicObject.InvoiceComponent;
import com.hand.basicObject.Rule.SubmitRuleItem;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
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
    private ExpenseReportPage expenseReportPage;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
        expenseReport =new ExpenseReport();
        expenseReportInvoice =new ExpenseReportInvoice();
        employee=getEmployee(phoneNumber,pwd,env);
        reimbSubmissionControl =new ReimbSubmissionControl();
        expenseReportPage = new ExpenseReportPage();
    }


    @Test(description = "报销提交管控-账套级-警告-费用类型管控-包含")
    public void submissionControlTest1() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee,rules,"自动化测试-日常报销单",employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1001);
        item.setControlCond(1001);
        item.setValueType(1006);
        reimbSubmissionControl.addRulesItem(employee,ruleOID,item,"自动化测试-报销标准");
        //创建报销单
        String reportOID =  expenseReportPage.setDailyReport(employee,UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",employee.getFullName());
        //新建费用
        String invoiceOid = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID);
        //检验标签
        assert expenseReportPage.checkSubmitLabel(employee,reportOID,"REPORT_SUBMIT_WARN",rules.getMessage());
        //删除规则
        deleteRules(ruleOID);
        expenseReport.deleteExpenseReport(employee,reportOID);
        expenseReportInvoice.deleteInvoice(employee,invoiceOid);
    }

    @Test(description = "报销提交管控-账套级-禁止-费用类型管控-包含")
    public void submissionControlTest2() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee,rules,"自动化测试-日常报销单",employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1001);
        item.setControlCond(1001);
        item.setValueType(1006);
        reimbSubmissionControl.addRulesItem(employee,ruleOID,item,"自动化测试-报销标准");
        //创建报销单
        String reportOID =  expenseReportPage.setDailyReport(employee,UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",employee.getFullName());
        //新建费用
        String invoiceOid = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID);
        //检验标签
        assert expenseReportPage.checkSubmitLabel(employee,reportOID,"REPORT_SUBMIT_ERROR",rules.getMessage());
        //删除规则
        deleteRules(ruleOID);
        expenseReport.deleteExpenseReport(employee,reportOID);
        expenseReportInvoice.deleteInvoice(employee,invoiceOid);
    }

    @Test(description = "报销提交管控-账套级-警告-费用类型管控-不包含")
    public void submissionControlTest3() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee,rules,"自动化测试-日常报销单",employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1001);
        item.setControlCond(1001);
        item.setValueType(1007);
        reimbSubmissionControl.addRulesItem(employee,ruleOID,item,"自动化测试-报销标准");
        //创建报销单
        String reportOID =  expenseReportPage.setDailyReport(employee,UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",employee.getFullName());
        //新建费用
        String invoiceOid = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID);
        //检验标签
        assert !expenseReportPage.checkSubmitLabel(employee,reportOID,"REPORT_SUBMIT_WARN",rules.getMessage());
        //删除规则
        deleteRules(ruleOID);
        expenseReport.deleteExpenseReport(employee,reportOID);
        expenseReportInvoice.deleteInvoice(employee,invoiceOid);
    }





    public void deleteRules(String ruleOID) throws HttpStatusException{
        reimbSubmissionControl.deleteReimbSubmissionRules(employee,ruleOID);

    }
}