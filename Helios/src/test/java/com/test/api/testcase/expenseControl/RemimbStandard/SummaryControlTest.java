package com.test.api.testcase.expenseControl.RemimbStandard;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardCondition;
import com.hand.basicObject.Rule.StandardControlItem;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/12/3
 * @Version 1.0
 **/
@Slf4j
public class SummaryControlTest extends BaseTest {

    private Employee employee;
    private ReimbStandard reimbStandard;
    private HashMap<String, String> map;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private ExpenseReportInvoice invoice;
    private StandardControl standardControl;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082971222") String phoneNumber, @Optional("zp123456") String pwd, @Optional("stage") String env){
        employee = getEmployee(phoneNumber,pwd,env);
        reimbStandard =new ReimbStandard();
        map = new HashMap<>();
        expenseReport = new ExpenseReport();
        expenseReportPage = new ExpenseReportPage();
        invoice = new ExpenseReportInvoice();
        standardControl = new StandardControl();
    }

    @BeforeMethod
    public void cleanMap(){
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准关闭-管控信息为：费用金额>基本标准")
    public void summaryControlTest01() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
    }

    @Test(description = "标准:账套级-汇总管控-禁止/费用参与人标准关闭-管控信息为：费用金额>基本标准")
    public void summaryControlTest02() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test summary control");
        rules.setControlModeType("SUMMARY");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5002",label);
    }

    @Test(description = "标准:公司级-汇总管控/费用参与人标准关闭-管控信息为：费用金额>基本标准")
    public void summaryControlTest03() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setLevelCode("COMPANY");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //        config base standard
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),false);
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准")
    public void summaryControlTest04() throws HttpStatusException {
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},250.00);
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*天数")
    public void summaryControlTest05() throws HttpStatusException {
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数
        StandardControlItem controlItem = standardControl.setStandardCondition("DAYS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},650.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*天数 开始结束日期为3天  则标准取就高的话200*3
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 650.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*人数")
    public void summaryControlTest06() throws HttpStatusException {
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*人数
        StandardControlItem controlItem = standardControl.setStandardCondition("PEOPLES");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*人数 参与人为俩人 则标准取就高的话200*2
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(2)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*房间数")
    public void summaryControlTest07() throws HttpStatusException {
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*房间数
        StandardControlItem controlItem = standardControl.setStandardCondition("ROOMS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*房间数 参与人为两个男人为1间房 则标准取就高的话200
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 250.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*月数")
    public void summaryControlTest08() throws HttpStatusException {
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数
        StandardControlItem controlItem = standardControl.setStandardCondition("MONTH_NUM");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},250.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*月数   则标准取就高的话200*1
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*出差天数")
    public void summaryControlTest09() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数
        StandardControlItem controlItem = standardControl.setStandardCondition("TRAVEL_DAYS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},1450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*出差天数   则标准取就高的话200*(开始-结束日期+1)=7
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 1450.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(7)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*天数+基本标准*人数")
    public void summaryControlTest10() throws HttpStatusException {
        StandardRules rules = standardControl.setSummaryHighRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数+基本标准*人数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition1 =new StandardCondition();
        standardCondition1.setOperatorId(1004);
        standardCondition1.setRightFieldCode("DAYS");
        StandardCondition standardCondition2 =new StandardCondition();
        standardCondition2.setOperatorId(1001);
        standardCondition2.setRightFieldCode("BASIC_STANDARD");
        StandardCondition standardCondition3 =new StandardCondition();
        standardCondition3.setOperatorId(1004);
        standardCondition3.setRightFieldCode("PEOPLES");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition1);
        conditions.add(standardCondition2);
        conditions.add(standardCondition3);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},1050.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*天数+基本标准*人数   则标准取和值的话350*3+350*2
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 1050.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(5)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准")
    public void summaryControlTest11() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},400.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取和值为150+200
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 400.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准*天数")
    public void summaryControlTest12() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数
        StandardControlItem controlItem = standardControl.setStandardCondition("DAYS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},1100.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取和值为(150+200)*3
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 1100.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准*人数")
    public void summaryControlTest13() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*人数
        StandardControlItem controlItem = standardControl.setStandardCondition("PEOPLES");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},750.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取和值为(150+200)*2
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 750.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(2)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准*房间数")
    public void summaryControlTest14() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*房间数
        StandardControlItem controlItem = standardControl.setStandardCondition("ROOMS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},400.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取和值为(150+200)*1
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 400.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准*月数")
    public void summaryControlTest15() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数（费用上的）
        StandardControlItem controlItem = standardControl.setStandardCondition("MONTH_NUM");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},400.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取和值为(150+200)*1
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 400.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准*出差天数")
    public void summaryControlTest16() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取和值 基本标准为350
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*出差天数
        StandardControlItem controlItem = standardControl.setStandardCondition("TRAVEL_DAYS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},2500.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*出差天数   则标准取就高的话350*报销单的(开始-结束日期+1)=7
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 2500.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(7)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取和值-管控信息为：费用金额>基本标准*天数+基本标准*人数")
    public void summaryControlTest17() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取和值 基本标准为350
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数+基本标准*人数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition1 =new StandardCondition();
        standardCondition1.setOperatorId(1004);
        standardCondition1.setRightFieldCode("DAYS");
        StandardCondition standardCondition2 =new StandardCondition();
        standardCondition2.setOperatorId(1001);
        standardCondition2.setRightFieldCode("BASIC_STANDARD");
        StandardCondition standardCondition3 =new StandardCondition();
        standardCondition3.setOperatorId(1004);
        standardCondition3.setRightFieldCode("PEOPLES");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition1);
        conditions.add(standardCondition2);
        conditions.add(standardCondition3);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},1800.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*出差天数   则标准取就高的话350*报销单的(开始-结束日期+1)=7
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 1800.00，超标：CNY 50.00。",rules.getMessage(),(standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(3)).add((standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(2))))));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准开启-管控信息为：费用金额>基本标准(城市组管控)")
    public void summaryControlTest18() throws HttpStatusException {
        //初始化规则
        StandardRules rules = standardControl.setSummarySumRule();
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,200,true,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{"auto_test_cityGroup1"});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,100,false,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{"auto_test_cityGroup2"});
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},250.00);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID1);
        //场景描述 基本标准两条 费用参与人只有auto_test_oneself命中城市组，auto_test_employee006未命中城市组 参与人中的员工006命中了上海 则标准取值为： 100+0 =100（城市组1为广州 2位上海）
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 150.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-设置方式为费用大类-管控信息为：费用金额>基本标准*天数")
    public void summaryControlTest19() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSummaryRule("HIGH","费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"餐饮");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数
        StandardControlItem controlItem = standardControl.setStandardCondition("DAYS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},650.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*天数 开始结束日期为3天  则标准取就高的话200*3
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 650.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        for (String s : map.keySet()){
            switch (s){
                case "ruleOID":
                    reimbStandard.deleteReimbStandardRules(employee, map.get("ruleOID"));
                    break;
                case "reportOID1":
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID1"));
                    break;
                case "reportOID2":
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID2"));
                    break;
                case "invoiceOID1":
                    invoice.deleteInvoice(employee, map.get("invoiceOID1"));
                    break;
                case "invoiceOID2":
                    invoice.deleteInvoice(employee, map.get("invoiceOID2"));
                    break;
            }
        }
    }
}
