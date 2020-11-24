//package com.test.api.testcase.expenseControl.SubmissionControl;
//
//import com.hand.baseMethod.HttpStatusException;
//import com.hand.basicObject.Employee;
//import com.hand.basicObject.Rule.SubmitRuleItem;
//import com.hand.basicObject.Rule.SubmitRules;
//import com.hand.utils.UTCTime;
//import com.test.BaseTest;
//import com.test.api.method.BusinessMethod.ExpenseReportPage;
//import com.test.api.method.ExpenseReport;
//import com.test.api.method.ExpenseReportInvoice;
//import com.test.api.method.ReimbSubmissionControl;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Optional;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
//
///**
// * @Author peng.zhang
// * @Date 2020/11/23
// * @Version 1.0
// **/
//public class FrequencyTest extends BaseTest {
//
//
//    private Employee employee;
//    private ExpenseReport expenseReport;
//    private ExpenseReportInvoice expenseReportInvoice;
//    private ReimbSubmissionControl reimbSubmissionControl;
//    private ExpenseReportPage expenseReportPage;
//
//
//    @BeforeClass
//    @Parameters({"phoneNumber", "passWord", "environment"})
//    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
//        expenseReport =new ExpenseReport();
//        expenseReportInvoice =new ExpenseReportInvoice();
//        employee=getEmployee(phoneNumber,pwd,env);
//        reimbSubmissionControl =new ReimbSubmissionControl();
//        expenseReportPage = new ExpenseReportPage();
//    }
//
//    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次")
//    public void submissionControlTest7() throws HttpStatusException {
//        SubmitRules rules = new SubmitRules();
//        rules.setName("报销提交管控-自动化");
//        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee,rules,"自动化测试-日常报销单",employee.getCompanyName());
//        //设置管控项
//        SubmitRuleItem item = new SubmitRuleItem();
//        item.setControlItem(1005);
//        item.setControlCond(1007);
//        item.setValueType(1002);
//        //按月
//        item.setMixedItem(2002);
//        //设置管控次数为1次
//        item.setFieldValue(1);
//        reimbSubmissionControl.addRulesItem(employee,ruleOID,item,"自动化测试-报销标准");
//        //创建报销单
//        String reportOID =  expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",employee.getFullName());
//        //新建费用
//        String invoiceOid1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID);
//        String invoiceOid2 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID);
//        //检验标签
//        assert !expenseReportPage.checkSubmitLabel(employee,reportOID,"REPORT_SUBMIT_WARN",rules.getMessage());
//        //删除规则
//        deleteRules(ruleOID);
//        expenseReport.deleteExpenseReport(employee,reportOID);
//        expenseReportInvoice.deleteInvoice(employee,invoiceOid1);
//        expenseReportInvoice.deleteInvoice(employee,invoiceOid2);
//    }
//}
