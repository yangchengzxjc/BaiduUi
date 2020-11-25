package com.test.api.testcase.expenseControl.SubmissionControl;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.SubmitRuleItem;
import com.hand.basicObject.Rule.SubmitRules;
import com.hand.basicconstant.submitControl.ControlCond;
import com.hand.basicconstant.submitControl.ControlItem;
import com.hand.basicconstant.submitControl.ControlMixedItem;
import com.hand.basicconstant.submitControl.ControlValueType;
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
        item.setControlItem(ControlItem.EXPENSE_TYPE.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_TYPE.getControlCond());
        item.setValueType(ControlValueType.INCLUDE.getValueType());
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-禁止-费用类型管控-包含")
    public void submissionControlTest2() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.EXPENSE_TYPE.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_TYPE.getControlCond());
        item.setValueType(ControlValueType.INCLUDE.getValueType());
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_ERROR", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-费用类型管控-不包含")
    public void submissionControlTest3() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.EXPENSE_TYPE.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_TYPE.getControlCond());
        item.setValueType(ControlValueType.UNINCLUDE.getValueType());
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID);
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert !expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-费用重复管控-包含")
    public void submissionControlTest4() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.SAME_COST_DATE.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_TYPE.getControlCond());
        item.setValueType(ControlValueType.INCLUDE.getValueType());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-禁止-费用重复管控-包含")
    public void submissionControlTest5() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.SAME_COST_DATE.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_TYPE.getControlCond());
        item.setValueType(ControlValueType.INCLUDE.getValueType());
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        //创建报销单
        String reportOID = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3), "自动化测试-日常报销单", employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(0,0));
        String invoiceOid2 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(0,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("invoiceOid2", invoiceOid2);
        map.put("ruleOID", ruleOID);
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_ERROR", rules.getMessage());
        String costMoth = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（ \"自动化测试-报销标准\"与\"自动化测试-报销标准\"在%s重复 ）。",rules.getMessage(),costMoth,costMoth);
        log.info("标签:{}",expect);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_ERROR",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/月")
    public void submissionControlTest6() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_TIMES.getControlItem());
        item.setControlCond(ControlCond.CUSTOM_NUMBER.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        //按月管控
        item.setMixedItem(ControlMixedItem.MONTH.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String costMoth = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 每月可报销1次，实报2次，已超出1次，详见：自动化测试-报销标准-%s、自动化测试-报销标准-%s。）",rules.getMessage(),costMoth,costMoth);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid2,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/天")
    public void submissionControlTest7() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_TIMES.getControlItem());
        item.setControlCond(ControlCond.CUSTOM_NUMBER.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        //按天管控
        item.setMixedItem(ControlMixedItem.DAY.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String costMoth = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 每天可报销1次，实报2次，已超出1次，详见：自动化测试-报销标准-%s、自动化测试-报销标准-%s。）",rules.getMessage(),costMoth,costMoth);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/季度")
    public void submissionControlTest8() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_TIMES.getControlItem());
        item.setControlCond(ControlCond.CUSTOM_NUMBER.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        //按月季度管控
        item.setMixedItem(ControlMixedItem.QUARTER.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String costMoth = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 每季度可报销1次，实报2次，已超出1次，详见：自动化测试-报销标准-%s、自动化测试-报销标准-%s。）",rules.getMessage(),costMoth,costMoth);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid2,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销次数管控-'>'1次/年")
    public void submissionControlTest9() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_TIMES.getControlItem());
        item.setControlCond(ControlCond.CUSTOM_NUMBER.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        //按月季度管控
        item.setMixedItem(ControlMixedItem.YEAR.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String costMoth = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 每年可报销1次，实报2次，已超出1次，详见：自动化测试-报销标准-%s、自动化测试-报销标准-%s。）",rules.getMessage(),costMoth,costMoth);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销单提交日期管控-'>'费用消费日期+1")
    public void submissionControlTest10() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_SUBMIT_DATE.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_COST_DATE.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        item.setMixedItem(ControlMixedItem.PLUS.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String expect = String.format("%s（%s\"%s\" 已超出 自动化测试-报销标准 %s \"%s\" 加%s天）",rules.getMessage(),ControlItem.REIMBURSEMENT_SUBMIT_DATE.getTypeName(),UTCTime.utcTObj(UTCTime.getNowUtcTime(),0),ControlCond.EXPENSE_COST_DATE.getTypeName(),UTCTime.utcTObj(UTCTime.getUtcTime(-2,0),0),item.getExtendValue());
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销单提交日期管控-'>'关联申请单结束日期")
    public void submissionControlTest11() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_SUBMIT_DATE.getControlItem());
        item.setControlCond(ControlCond.ASSOCIATED_APPLICATION_LATEST_END_DATE.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        item.setMixedItem(ControlMixedItem.PLUS.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
    }

    @Test(description = "报销提交管控-账套级-警告-报销单提交月管控-'>'费用消费月+0")
    public void submissionControlTest12() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        item.setControlItem(ControlItem.REIMBURSEMENT_SUBMIT_MONTH.getControlItem());
        item.setControlCond(ControlCond.EXPENSE_COST_MONTH.getControlCond());
        item.setValueType(ControlValueType.GT.getValueType());
        item.setMixedItem(ControlMixedItem.PLUS.getMixItem());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String costMoth = UTCTime.utcTObjmoth(UTCTime.getUtcTime(-31,0),0);
        String expect = String.format("%s（%s\"%s\" 已超出 自动化测试-报销标准 %s \"%s ~ %s\" ）",rules.getMessage(),ControlItem.REIMBURSEMENT_SUBMIT_MONTH.getTypeName(),UTCTime.utcTObjmoth(UTCTime.getNowUtcTime(),0),ControlCond.EXPENSE_COST_MONTH.getTypeName(),costMoth,costMoth);
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用城市管控-'不归属'关联申请单的行程城市")
    public void submissionControlTest13() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单内的费用城市管控
        item.setControlItem(ControlItem.REIMBURSEMENT_INVOICE_CITY.getControlItem());
        //关联申请单的行程城市
        item.setControlCond(ControlCond.ASSOCIATED_APPLICATION_TRAVEl_CITY.getControlCond());
        //不归属于
        item.setValueType(ControlValueType.UN_BELONG_TO.getValueType());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String expect = String.format("%s（自动化测试-报销标准 城市\"上海\" 不在%s 范围内）",rules.getMessage(),ControlCond.ASSOCIATED_APPLICATION_TRAVEl_CITY.getTypeName());
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用的消费日期-'不归属'关联申请单的行程日期")
    public void submissionControlTest14() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单费用消费日期
        item.setControlItem(ControlItem.REIMBURSEMENT_COST_DATE.getControlItem());
        //关联申请单的行程日期
        item.setControlCond(ControlCond.ASSOCIATED_APPLICATION_TRAVEl_DATE.getControlCond());
        //不归属于
        item.setValueType(ControlValueType.UN_BELONG_TO.getValueType());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String itDate = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 %s\"%s ~ %s\" 已超出 %s）",rules.getMessage(),ControlItem.REIMBURSEMENT_COST_DATE.getTypeName(),itDate,itDate,ControlCond.ASSOCIATED_APPLICATION_TRAVEl_DATE.getTypeName());
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用的消费日期-'不归属'关联申请单的起止日期")
    public void submissionControlTest15() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "差旅报销单-自动化测试", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单费用消费日期
        item.setControlItem(ControlItem.REIMBURSEMENT_COST_DATE.getControlItem());
        //关联申请单的起止日期
        item.setControlCond(ControlCond.ASSOCIATED_APPLICATION_START_END_DATE.getControlCond());
        //不归属于
        item.setValueType(ControlValueType.UN_BELONG_TO.getValueType());
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
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String itDate = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 %s\"%s ~ %s\" 已超出 %s）",rules.getMessage(),ControlItem.REIMBURSEMENT_COST_DATE.getTypeName(),itDate,itDate,ControlCond.ASSOCIATED_APPLICATION_START_END_DATE.getTypeName());
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
    }

    @Test(description = "报销提交管控-账套级-警告-报销单费用的消费日期-'不归属'报销单起止日期")
    public void submissionControlTest16() throws HttpStatusException {
        SubmitRules rules = new SubmitRules();
        rules.setName("报销提交管控-自动化");
        String ruleOID = reimbSubmissionControl.addReimbSubmissionControl(employee, rules, "自动化测试-日常报销单", employee.getCompanyName());
        //设置管控项
        SubmitRuleItem item = new SubmitRuleItem();
        //报销单费用消费日期
        item.setControlItem(ControlItem.REIMBURSEMENT_COST_DATE.getControlItem());
        //报销单起止日期
        item.setControlCond(ControlCond.REIMBURSEMENT_START_END_DATE.getControlCond());
        //不归属于
        item.setValueType(ControlValueType.UN_BELONG_TO.getValueType());
        reimbSubmissionControl.addRulesItem(employee, ruleOID, item, "自动化测试-报销标准");
        String reportOID = expenseReportPage.setDailyReport(employee,UTCTime.getUTCDateEnd(-1),"自动化测试-日常报销单",employee.getFullName());
        //新建费用
        String invoiceOid1 = expenseReportPage.setInvoice(employee, "自动化测试-报销标准", reportOID,UTCTime.getUtcTime(0,0));
        //检验标签
        map.put("reportOID", reportOID);
        map.put("invoiceOid1", invoiceOid1);
        map.put("ruleOID", ruleOID);
        assert expenseReport.checkSubmitLabel(employee, reportOID, "REPORT_SUBMIT_WARN", rules.getMessage());
        String itDate = UTCTime.utcTObj(UTCTime.getUtcTime(0,0),0);
        String expect = String.format("%s（自动化测试-报销标准 %s\"%s ~ %s\" 已超出 %s）",rules.getMessage(),ControlItem.REIMBURSEMENT_COST_DATE.getTypeName(),itDate,itDate,ControlCond.REIMBURSEMENT_START_END_DATE.getTypeName());
        assert expenseReportInvoice.checkInvoiceLabel(employee,invoiceOid1,"REPORT_SUBMIT_WARN",expect);
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