package com.test.api.testcase.expenseControl.RemimbStandard;

import com.google.gson.JsonArray;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.submitControl.ControlItem;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.StandardControlItem;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.hand.basicObject.component.InvoiceComponent;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2020/12/3
 * @Version 1.0
 **/
@Slf4j
public class PeriodControlTest extends BaseTest {

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

    @Test(description = "规则配置:账套级-周期管控/每天-费用金额>基本标准")
    public void periodControlTest01() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setPeriod("DAY","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //配置基本标准：200
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String date = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),date,standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
    }

    @Test(description = "规则配置:账套级-周期管控/每月-费用金额>基本标准")
    public  void periodControlTest02()throws HttpStatusException{
        //新建账套级规则
        StandardRules rules = standardControl.setPeriod("MONTH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new  String[]{},new String[]{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //配置基本标准:200
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String data = UTCTime.utcTObjmonth(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),data,standardRulesItem.getAmount());
        log.info("标签：{}",label);
        assert expenseReport.checkSubmitLabel(employee,reportOID1,"5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "规则配置：账套级-周期管控/每季度-费用金额>基本标准")
    public void periodControlTest03()throws HttpStatusException{
        //新建账套级规则
        StandardRules rules = standardControl.setPeriod("QUARTER","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String[]{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //配置基本标准：200
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName()},250.00);
        String invoiceOID2 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName()},250.00);
        map.put("ruleOID",ruleOID);
        map.put("invoiceOID1",invoiceOID1);
        map.put("invoiceOID2",invoiceOID2);
        map.put("reportOID1",reportOID1);
        String year = UTCTime.utcTObjyear(UTCTime.getUtcTime(0,0),0);
        String quarter = UTCTime.isQuarter(UTCTime.getUtcTime(0,0));
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 500.00，超标：CNY 300.00。",rules.getMessage(),year+quarter,standardRulesItem.getAmount());
        log.info("标签：{}",label);
        assert expenseReport.checkSubmitLabel(employee,reportOID1,"5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID2,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }

    @Test(description = "规则配置：账套级-周期管控/每年-费用金额>基本标准")
    public void periodControlTest04()throws HttpStatusException{
        //新建账套级规则
        StandardRules rules = standardControl.setPeriod("YEAR","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String[]{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //配置基本标准：200
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName()},250.00);
        String invoiceOID2 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName()},250.00);
        map.put("ruleOID",ruleOID);
        map.put("invoiceOID1",invoiceOID1);
        map.put("invoiceOID2",invoiceOID2);
        map.put("reportOID1",reportOID1);
        String date = UTCTime.utcTObjyear(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 500.00，超标：CNY 300.00。",rules.getMessage(),date+"年",standardRulesItem.getAmount());
        log.info("标签：{}",label);
        assert expenseReport.checkSubmitLabel(employee,reportOID1,"5001",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
        assert invoice.checkInvoiceLabel(employee,invoiceOID2,"EXPENSE_STANDARD_EXCEEDED_WARN",label);
    }


    @Test(description = "规则配置:账套级-周期管控-费用大类管控/每天-费用金额>基本标准")
    public void periodControlTest05() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setPeriod("DAY","费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"餐饮");
        //配置基本标准：200
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String date = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),date,standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
    }

    @Test(description = "规则配置：账套级-周期管控/每天-平均金额>基本标准")
    public void periodControlTest06() throws HttpStatusException{
        //新建账套级规则
        StandardRules rules = standardControl.setPeriod("DAY","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //配置管控项：平均金额
        StandardControlItem standardControlItem = new StandardControlItem();
        standardControlItem.setControlItem("AVERAGE_AMOUNT");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,standardControlItem);
        //配置基本标准
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,100,true,rules,ruleOID,new String[]{},new String[]{});
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,1,2,new String[]{employee.getFullName(),"员工0006"},2000);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        String date1 = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String date2 = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),1);
        String label = String.format("员工0006 %s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 200.00，超标：CNY 100.00。1024bugfix员工0006 %s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 200.00，超标：CNY 100.00。1024bugfix%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 200.00，超标：CNY 100.00。1024bugfix%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 200.00，超标：CNY 100.00。",
                rules.getMessage(),date1,standardRulesItem.getAmount(),rules.getMessage(),date2,standardRulesItem.getAmount(),
                rules.getMessage(),date1,standardRulesItem.getAmount(),rules.getMessage(),date2,standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, reportOID1, "5001",label);
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
