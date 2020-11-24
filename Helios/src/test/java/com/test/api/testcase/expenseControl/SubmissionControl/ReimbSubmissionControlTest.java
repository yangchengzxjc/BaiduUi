package com.test.api.testcase.expenseControl.SubmissionControl;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.SubmitRuleItem;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.ApplicationMethod.TravelApplicationPage;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
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
    private HashMap<String, String> map;


    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env) {
        expenseReport = new ExpenseReport();
        expenseReportInvoice = new ExpenseReportInvoice();
        employee = getEmployee(phoneNumber, pwd, env);
        reimbSubmissionControl = new ReimbSubmissionControl();
        expenseReportPage = new ExpenseReportPage();
        map = new HashMap<>();
    }

    @BeforeMethod
    public void cleanMap() {
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "报销提交管控-账套级-警告-费用类型管控-包含")
    public void submissionControlTest1() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1001);
        item.setControlCond(1001);
        item.setValueType(1006);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-禁止-费用类型管控-包含")
    public void submissionControlTest2() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1001);
        item.setControlCond(1001);
        item.setValueType(1006);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_ERROR", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-费用类型管控-不包含")
    public void submissionControlTest3() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1001);
        item.setControlCond(1001);
        item.setValueType(1007);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert !expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-费用重复管控-包含")
    public void submissionControlTest4() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1002);
        item.setControlCond(1001);
        item.setValueType(1006);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-禁止-费用重复管控-包含")
    public void submissionControlTest5() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1002);
        item.setControlCond(1001);
        item.setValueType(1006);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_ERROR", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/月")
    public void submissionControlTest6() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1005);
        item.setControlCond(1007);
        item.setValueType(1002);
        //按月管控
        item.setMixedItem(2002);
        //设置管控次数为1次
        item.setFieldValue(1);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/天")
    public void submissionControlTest7() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1005);
        item.setControlCond(1007);
        item.setValueType(1002);
        //按天管控
        item.setMixedItem(2001);
        //设置管控次数为1次
        item.setFieldValue(1);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/季度")
    public void submissionControlTest8() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1005);
        item.setControlCond(1007);
        item.setValueType(1002);
        //按月季度管控
        item.setMixedItem(2003);
        //设置管控次数为1次
        item.setFieldValue(1);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/年")
    public void submissionControlTest9() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1005);
        item.setControlCond(1007);
        item.setValueType(1002);
        //按月季度管控
        item.setMixedItem(2004);
        //设置管控次数为1次
        item.setFieldValue(1);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单提交日期管控-'>'费用消费日期+1")
    public void submissionControlTest10() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1006);
        item.setControlCond(1002);
        item.setValueType(1002);
        item.setMixedItem(1001);
        item.setExtendValue("1");
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        // 创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        // 新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(-2,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单提交日期管控-'>'关联申请单结束日期")
    public void submissionControlTest11() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1006);
        item.setControlCond(1004);
        item.setValueType(1002);
        item.setMixedItem(1001);
        item.setExtendValue(1);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //新建差旅申请单
        TravelApplicationPage travelApplicationPage =new TravelApplicationPage();
        String applicatioOID = travelApplicationPage.setTravelApplication(employee,"差旅申请单-自动化测试",UTCTime.getUTCDateEnd(-2));
        //报销单
        String reportOID = expenseReportPage.setTravelReport(employee,"差旅报销单-自动化测试",applicatioOID);
        // 新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单提交月管控-'>='费用消费日期+0")
    public void submissionControlTest12() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(1007);
        item.setControlCond(1002);
        item.setValueType(1003);
        item.setMixedItem(1001);
        item.setExtendValue(0);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        // 创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        // 新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(-31,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用城市管控-'不归属'关联申请单的行程城市")
    public void submissionControlTest13() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单内的费用城市管控
        item.setControlItem(1009);
        //关联申请单的行程城市
        item.setControlCond(1008);
        //不归属于
        item.setValueType(1012);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //新建差旅申请单
        TravelApplicationPage travelApplicationPage =new TravelApplicationPage();
        String applicatioOID = travelApplicationPage.setTravelApplication(employee,"差旅申请单-自动化测试",UTCTime.getUTCDateEnd(-2));
        //报销单
        String reportOID = expenseReportPage.setTravelReport(employee,"差旅报销单-自动化测试",applicatioOID);
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用的消费日期-'不归属'关联申请单的行程日期")
    public void submissionControlTest14() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单费用消费日期
        item.setControlItem(1010);
        //关联申请单的行程日期
        item.setControlCond(1010);
        //不归属于
        item.setValueType(1012);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //新建差旅申请单
        TravelApplicationPage travelApplicationPage =new TravelApplicationPage();
        String applicatioOID = travelApplicationPage.setTravelApplication(employee,"差旅申请单-自动化测试",UTCTime.getUTCDateEnd(-2));
        //报销单
        String reportOID = expenseReportPage.setTravelReport(employee,"差旅报销单-自动化测试",applicatioOID);
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(0,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用的消费日期-'不归属'关联申请单的起止日期")
    public void submissionControlTest15() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单费用消费日期
        item.setControlItem(1010);
        //关联申请单的起止日期
        item.setControlCond(1012);
        //不归属于
        item.setValueType(1012);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //新建差旅申请单
        TravelApplicationPage travelApplicationPage =new TravelApplicationPage();
        String applicatioOID = travelApplicationPage.setTravelApplication(employee,"差旅申请单-自动化测试",UTCTime.getUTCDateEnd(-2));
        //报销单
        String reportOID = expenseReportPage.setTravelReport(employee,"差旅报销单-自动化测试",applicatioOID);
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(0,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用的消费日期-'不归属'报销单起止日期")
    public void submissionControlTest16() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单费用消费日期
        item.setControlItem(1010);
        //报销单起止日期
        item.setControlCond(1011);
        //不归属于
        item.setValueType(1012);
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        String reportOID = expenseReportPage.setDailyReport(employee,UTCTime.getUTCDateEnd(-1),"自动化测试-日常报销单",employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(0,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReportPage.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }


    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        for (String s : map.keySet()) {
            switch (s) {
                case "ruleOID":
                    reimbSubmissionControl.deleteReimbSubmissionRules(employee, map.get("ruleOID"));
                    break;
                case "reportOID":
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID"));
                    break;
                case "invoiceOid1":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOid1"));
                    break;
                case "invoiceOid2":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOid2"));
                    break;
            }
        }
    }
}