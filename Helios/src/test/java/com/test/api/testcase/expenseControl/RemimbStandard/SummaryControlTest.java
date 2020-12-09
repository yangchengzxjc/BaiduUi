package com.test.api.testcase.expenseControl.RemimbStandard;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardCondition;
import com.hand.basicObject.Rule.StandardControlItem;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import lombok.extern.slf4j.Slf4j;
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
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
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
    public void summaryControlTest1() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
    }


    @Test(description = "标准:账套级-汇总管控-禁止/费用参与人标准关闭-管控信息为：费用金额>基本标准")
    public void summaryControlTest2() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test summary control");
        rules.setControlModeType("SUMMARY");
        rules.setControlLevel("FORBID");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5002",label);
    }

    @Test(description = "标准:公司级-汇总管控/费用参与人标准关闭-管控信息为：费用金额>基本标准")
    public void summaryControlTest3() throws HttpStatusException {
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
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准")
    public void summaryControlTest4() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},250.00);
        map.put("invoiceOID1",invoiceOID1);
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*天数")
    public void summaryControlTest5() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition =new StandardCondition();
        standardCondition.setOperatorId(1004);
        standardCondition.setRightFieldCode("DAYS");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},650.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*天数 开始结束日期为3天  则标准取就高的话200*3
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 650.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*人数")
    public void summaryControlTest6() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*人数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition =new StandardCondition();
        standardCondition.setOperatorId(1004);
        standardCondition.setRightFieldCode("PEOPLES");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*人数 参与人为俩人 则标准取就高的话200*2
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount().multiply(new BigDecimal(2)));
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*房间数")
    public void summaryControlTest7() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*房间数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition =new StandardCondition();
        standardCondition.setOperatorId(1004);
        standardCondition.setRightFieldCode("ROOMS");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*房间数 参与人为两个男人为1间房 则标准取就高的话200
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 250.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*月数")
    public void summaryControlTest8() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition =new StandardCondition();
        standardCondition.setOperatorId(1004);
        standardCondition.setRightFieldCode("MONTH_NUM");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},250.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准*月数   则标准取就高的话200*1
        String label = String.format("%s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "标准:账套级-汇总管控/费用参与人标准取就高-管控信息为：费用金额>基本标准*出差天数")
    public void summaryControlTest9() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = new StandardRules();
        rules.setName("auto test period control");
        rules.setControlModeType("SUMMARY");
        rules.setParticipantsEnable(true);
        //取就高
        rules.setParticipantsMode("HIGH");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard 取就高 标准为200
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数
        StandardControlItem controlItem = new StandardControlItem();
        StandardCondition standardCondition =new StandardCondition();
        standardCondition.setOperatorId(1004);
        standardCondition.setRightFieldCode("TRAVEL_DAYS");
        ArrayList<StandardCondition> conditions =new ArrayList<>();
        conditions.add(standardCondition);
        controlItem.setConditions(conditions);
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
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


//    @AfterMethod
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
