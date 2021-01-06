package com.test.api.testcase.expenseControl.RemimbStandard;

import com.hand.basicConstant.submitControl.ControlValueType;
import com.hand.basicObject.Rule.StandardControlItem;
import com.hand.basicObject.Rule.StandardRules;
import com.hand.basicObject.Rule.StandardRulesItem;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.UTCTime;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseControlMethod.StandardControl;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReimbStandard;
import com.hand.baseMethod.HttpStatusException;
import com.hand.basicObject.Employee;
import com.test.BaseTest;
import lombok.extern.slf4j.Slf4j;

import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.HashMap;

@Slf4j
public class SingleControlTest extends BaseTest {


    private Employee employee;
    private ReimbStandard reimbStandard;
    private HashMap<String, String> map;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private StandardControl standardControl;
    private ExpenseReportInvoice invoice;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env){
        employee = getEmployee(phoneNumber,pwd,env);
        reimbStandard =new ReimbStandard();
        expenseReport = new ExpenseReport();
        expenseReportPage = new ExpenseReportPage();
        map = new HashMap<>();
        standardControl = new StandardControl();
        invoice = new ExpenseReportInvoice();
    }

    @BeforeMethod
    public void cleanMap(){
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准关闭-管控信息为：费用金额>基本标准")
    public void singleControlTest01() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID1);
        String data = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),data,standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用金额>基本标准")
    public void singleControlTest02() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID1);
        String data = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),data,standardRulesItem2.getAmount());
        log.info("标签:{}",label);
        Assert.assertEquals(label,expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"));
        Assert.assertEquals(label,invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"));
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用金额>基本标准*天数")
    public void singleControlTest03() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数
        StandardControlItem controlItem = standardControl.setStandardCondition("DAYS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},650.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取就高200*3
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 650.00，超标：CNY 50.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用金额>基本标准*人数")
    public void singleControlTest04() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*天数
        StandardControlItem controlItem = standardControl.setStandardCondition("PEOPLES");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取就高200*2
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 50.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().multiply(new BigDecimal(2)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用金额>基本标准*房间数")
    public void singleControlTest05() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*房间数
        StandardControlItem controlItem = standardControl.setStandardCondition("ROOMS");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取就高200*1
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 250.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().multiply(new BigDecimal(1)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }


    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用金额>基本标准*月数")
    public void singleControlTest06() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数
        StandardControlItem controlItem = standardControl.setStandardCondition("MONTH_NUM");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取就高200*1
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 450.00，超标：CNY 250.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().multiply(new BigDecimal(1)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：备注-包括(费用事由)")
    public void singleControlTest07() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("INVOICE_COMMENT", ControlValueType.INCLUDE.getValueType(),"事由","STRING");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),rules.getMessage());
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：申请人组-包括(人员组)")
    public void singleControlTest08() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("APPLY_USER", ControlValueType.INCLUDE.getValueType(),"auto_test_oneself","USER_GROUP");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),rules.getMessage());
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：参与人组-包括(人员组)")
    public void singleControlTest09() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("PARTICIPANT_USER", ControlValueType.INCLUDE.getValueType(),"auto_test_employee006","USER_GROUP");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName(),"员工0006"});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),rules.getMessage());
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用附件为空")
    public void singleControlTest10() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("INVOICE_ATTACHMENT", ControlValueType.IS_NULL.getValueType(),null,"STRING");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},450.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),rules.getMessage());
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用附件为空&& 费用金额>基本标准必须满足")
    public void singleControlTest11() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置基本的标准
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,200,true,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为备注包括 费用附件为空和 费用金额>基本标准
        StandardControlItem controlItem1 = standardControl.setStandControlItem("INVOICE_ATTACHMENT", ControlValueType.IS_NULL.getValueType(),null,"STRING");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem1);
        //设置一个费用金额的标准
        StandardControlItem controlItem2 = new StandardControlItem();
        reimbStandard.editORaddControlItem(employee,false,rules,ruleOID,controlItem2);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName()},100.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),"");
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用标签-包含-公司已付")
    public void singleControlTest12() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("INVOICE_LABEL", ControlValueType.INCLUDE.getValueType(),"20","INVOICE_LABEL");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
         FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
         //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),true);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),rules.getMessage());
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用标签-不包含-电子票")
    public void singleControlTest13() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("INVOICE_LABEL", ControlValueType.UNINCLUDE.getValueType(),"9","INVOICE_LABEL");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),true);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"5001"),rules.getMessage());
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为：费用标签-包含-替票")
    public void singleControlTest14() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("INVOICE_LABEL", ControlValueType.INCLUDE.getValueType(),"32","INVOICE_LABEL");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,reportOID1,"5001"),rules.getMessage());
    }


    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为:天数>2")
    public void singleControlTest15() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //设置管控项为备注包括
        StandardControlItem controlItem = standardControl.setStandControlItem("DURATION_DAYS", ControlValueType.GT.getValueType(),2,"LONG");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",reportOID1);
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName()},200.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,reportOID1,"5001"),rules.getMessage());
    }

//    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取就高-管控信息为:房间数>1&&>基本标准")
//    public void singleControlTest16() throws HttpStatusException {
//        //新建账套级规则
//        StandardRules rules = standardControl.setSingleRule("HIGH","费用类型");
//        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
//        map.put("ruleOID",ruleOID);
//        //设置基本标准200元
//        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
//        //设置管控项为备注包括
//        StandardControlItem controlItem1 = standardControl.setStandControlItem("ROOMS", ControlValueType.GT.getValueType(),1,"LONG");
//        reimbStandard.editORaddControlItem(employee,false,rules,ruleOID,controlItem1);
//        //新建报销单
//        String reportOID1 = expenseReportPage.setDailyReport(employee,"自动化测试-日常报销单",new String[]{employee.getFullName()}).get("expenseReportOID");
//        map.put("reportOID1",reportOID1);
//        //新建费用
//        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",reportOID1,new String[]{employee.getFullName(),"yuuki"},250.00);
//        map.put("invoiceOID1",invoiceOID1);
//        Assert.assertEquals(expenseReport.checkSubmitLabel(employee,reportOID1,"5001"),rules.getMessage());
//    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取和值-管控信息为：费用金额>基本标准*天数")
    public void singleControlTest17() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("SUM","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
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
        //管控项为基本标准   则标准取就高350*3
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 1100.00，超标：CNY 50.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取取和值-管控信息为：费用金额>基本标准*房间数")
    public void singleControlTest18() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("SUM","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
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
        //管控项为基本标准   则标准取和值350*1
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 400.00，超标：CNY 50.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(1)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @Test(description = "标准:账套级-单条管控-费用参与人标准开启取和值-管控信息为：费用金额>基本标准*月数")
    public void singleControlTest19() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("SUM","费用类型");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"自动化测试-报销标准");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem1 = standardControl.setStandardRulesItem(employee,150,true,rules,ruleOID,new String[]{"auto_test_employee006"},new String[]{});
        StandardRulesItem standardRulesItem2 = standardControl.setStandardRulesItem(employee,200,false,rules,ruleOID,new String[]{"auto_test_oneself"},new String[]{});
        //设置管控项为费用金额>基本标准*月数
        StandardControlItem controlItem = standardControl.setStandardCondition("MONTH_NUM");
        reimbStandard.editORaddControlItem(employee,true,rules,ruleOID,controlItem);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID1",formDetail.getReportOID());
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),new String[]{employee.getFullName(),"员工0006"},400.00);
        map.put("invoiceOID1",invoiceOID1);
        //管控项为基本标准   则标准取就高350*1
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 400.00，超标：CNY 50.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(1)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @Test(description = "标准:账套级-单条管控-费用大类-管控信息为：费用金额>基本标准")
    public void singleControlTest20() throws HttpStatusException {
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"餐饮");
        //config base standard, exist default control item
        StandardRulesItem standardRulesItem = standardControl.setStandardRulesItem(employee,true,rules,ruleOID);
        //新建报销单
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        //新建费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"自动化测试-报销标准",formDetail.getReportOID(),false);
        map.put("ruleOID",ruleOID);
        map.put("reportOID1",formDetail.getReportOID());
        map.put("invoiceOID1",invoiceOID1);
        String data = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 250.00，超标：CNY 50.00。",rules.getMessage(),data,standardRulesItem.getAmount());
        log.info("标签:{}",label);
        assert expenseReport.checkSubmitLabel(employee, formDetail.getReportOID(), "5001",label);
    }

    @Test(description = "标准:账套级-单条管控-费用大类-管控信息为：费用金额>基本标准*天数")
    public void singleControlTest21() throws HttpStatusException{
        //新建账套级规则
        StandardRules rules = standardControl.setSingleRule("SUM","费用大类");
        String ruleOID = reimbStandard.addReimbstandard(employee,rules,new String[]{},new String []{"自动化测试-日常报销单"},"餐饮");
        map.put("ruleOID",ruleOID);
        //config base standard, exist default control item
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
        //管控项为基本标准   则标准取就高350*3
        String itDate = UTCTime.utcTOday(UTCTime.getUtcTime(0,0),0);
        String label = String.format("%s %s 自动化测试-报销标准 标准为：CNY %s.00，已使用：CNY 1100.00，超标：CNY 50.00。",rules.getMessage(),itDate,standardRulesItem2.getAmount().add(standardRulesItem1.getAmount()).multiply(new BigDecimal(3)));
        log.info("标签:{}",label);
        Assert.assertEquals(expenseReport.checkSubmitLabel(employee, reportOID1, "5001"),label);
        Assert.assertEquals(invoice.checkInvoiceLabel(employee,invoiceOID1,"EXPENSE_STANDARD_EXCEEDED_WARN"),label);
    }

    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        ExpenseReportInvoice invoice =new ExpenseReportInvoice();
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
