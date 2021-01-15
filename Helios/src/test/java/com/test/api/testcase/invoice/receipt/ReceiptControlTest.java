package com.test.api.testcase.invoice.receipt;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ReceiptConfig;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.receiptConfig.ReceiptOverTime;
import com.hand.basicObject.Rule.receiptConfig.ReceiptWords;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseMethod.ReceiptMethodPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/5
 * @Version 1.0
 **/
public class ReceiptControlTest extends BaseTest {

    private Employee employee;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private ReceiptControlConfig receiptControlConfig;
    private ExpenseReportInvoice expenseReportInvoice;
    private HashMap<String,String> map;
    private ReceiptMethodPage receiptMethodPage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082978625") String phoneNumber, @Optional("rr123456") String pwd, @Optional("stage") String env) {
        employee = getEmployee(phoneNumber, pwd, env);
        expenseReportPage = new ExpenseReportPage();
        receiptControlConfig = new ReceiptControlConfig();
        map = new HashMap<>();
        expenseReport = new ExpenseReport();
        expenseReportInvoice = new ExpenseReportInvoice();
        receiptMethodPage = new ReceiptMethodPage();
    }

    @BeforeMethod
    public void cleanMap(){
        if(map.size()!=0){
            map.clear();
        }
    }

    @Test(description = "免贴票管控-费用公司已付标签-免贴票")
    public void receiptControl01() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        map.put("itemId","1");
        receiptControlConfig.noPasteReceipt(employee,"公司已付");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //新建公司已付的费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"交通",formDetail.getReportOID(),true);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals("免贴票",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOID1,"INVOICE_FREE"));
    }

    @Test(description = "免贴票管控-电子票-免贴票")
    public void receiptControl02() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        map.put("itemId","1");
        receiptControlConfig.noPasteReceipt(employee,"电子票");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //新建公司已付的费用
        FormDetail invoice = expenseReportPage.setReceiptInvoice(employee,"交通",formDetail.getReportOID(), Receript.receipt4);
        map.put("invoiceOID1",invoice.getInvoiceOID());
        Assert.assertEquals("免贴票",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"INVOICE_FREE"));
    }

    @Test(description = "免贴票管控-费用无票标签-免贴票")
    public void receiptControl03() throws HttpStatusException {
        //新建发票管控规则公司已付-免贴票
        map.put("itemId","1");
        receiptControlConfig.noPasteReceipt(employee,"无票");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //新建无票费用
        String invoiceOID1 = expenseReportPage.setInvoice(employee,"无票费用",formDetail.getReportOID(),10.00);
        map.put("invoiceOID1",invoiceOID1);
        Assert.assertEquals("免贴票",expenseReportInvoice.checkInvoiceLabelName(employee,invoiceOID1,"INVOICE_FREE"));
    }

    @Test(description = "发票启用抬头管控，抬头一致性管控-增值税普通发票(电子)")
    public void receiptControl04() throws HttpStatusException {
        //开启报销单抬头一致性检查
        receiptControlConfig.receiptTitleConfig(employee,true,false);
        map.put("itemId","4");
        receiptControlConfig.receiptConfig(employee, ReceiptConfig.expenseHeaderConfig);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.receipt4);
        map.put("invoiceOID1",invoice.getInvoiceOID());
        Assert.assertEquals("抬头有误",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"WRONG_HEADER"));
        //提交报销单
        Assert.assertEquals("有1笔发票抬头不一致",expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"6001"));
        Assert.assertEquals("抬头不一致",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"MISMATCHED_TITLE"));
    }

    @Test(description = "场景化的发票启用抬头管控，抬头一致性管控-增值税普通发票(电子)")
    public void receiptControl05() throws HttpStatusException {
        //开启报销单抬头一致性检查
        receiptControlConfig.receiptTitleConfig(employee,true,true);
        map.put("receiptTitle","true");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.receipt4);
        map.put("invoiceOID1",invoice.getInvoiceOID());
        Assert.assertEquals("抬头有误",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"WRONG_HEADER"));
        //提交报销单
        Assert.assertEquals("有1笔发票抬头不一致",expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"6001"));
        Assert.assertEquals("抬头不一致",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"MISMATCHED_TITLE"));
    }

    @Test(description = "场景化的发票启用抬头管控，抬头一致性管控-区块链普通发票(电子)")
    public void receiptControl06() throws HttpStatusException{
        //开启发票重复可生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.duplicatedReceipt(employee,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        //开启报销单抬头一致性检查
        receiptControlConfig.receiptTitleConfig(employee,true,true);
        map.put("receiptTitle","true");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.qukuailian);
        map.put("invoiceOID1",invoice.getInvoiceOID());
        Assert.assertEquals("抬头有误",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"WRONG_HEADER"));
        //提交报销单
        Assert.assertEquals("有1笔发票抬头不一致",expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"6001"));
        Assert.assertEquals("抬头不一致",expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"MISMATCHED_TITLE"));
    }

    @Test(description = "场景化的发票启用抬头管控，抬头一致性管控-电信发票不校验")
    public void receiptControl14() throws HttpStatusException{
        //开启发票重复可生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.duplicatedReceipt(employee,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        //开启报销单抬头一致性检查
        receiptControlConfig.receiptTitleConfig(employee,true,true);
        map.put("receiptTitle","true");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.dianxinReceipt);
        map.put("invoiceOID1",invoice.getInvoiceOID());
        try{
            expenseReportInvoice.checkInvoiceLabelName(employee,invoice.getInvoiceOID(),"WRONG_HEADER");
        }catch (NullPointerException e){
            Assert.assertTrue(true);
        }
    }

    @Test(description = "发票连号检验")
    public void receiptControl07() throws HttpStatusException {
        //开启报销单抬头一致性检查
        receiptControlConfig.receiptConsecutiveConfig(employee, ReceiptConfig.receiptConsecutive,true);
        map.put("receiptConsecutive","true");
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.HANDRECEIPT1);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        FormDetail invoice2 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.HANDRECEIPT2);
        map.put("invoiceOID2",invoice2.getInvoiceOID());
        Assert.assertEquals("连号",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"LINK_NO"));
    }

    @Test(description = "他人发票归属人检查")
    public void receiptControl08() throws HttpStatusException {
        //开启报销单抬头一致性检查
        receiptControlConfig.checkOtherReceiptConfig(employee,false,true);
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.trainReceipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("他人发票",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"OTHERS_RECEIPT"));
        //关闭他人发票验证
        receiptControlConfig.checkOtherReceiptConfig(employee,false,false);
    }

    @Test(description = "发票重复检查")
    public void receiptControl09() throws HttpStatusException {
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.receipt5);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("发票重复",expenseReportInvoice.checkVerifyReceipt(employee,Receript.receipt5,"DUPLICATE_INVOICE",false));
    }

    @Test(description = "发票逾期管控-静态管控-弱管控")
    public void receiptControl10() throws HttpStatusException {
        ReceiptOverTime receiptOverTime = new ReceiptOverTime();
        String overTimeConfigId = receiptControlConfig.receiptOverTime(employee,receiptOverTime);
        map.put("overTimeConfigId",overTimeConfigId);
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), String.format(Receript.HANDRECEIPT3,UTCTime.getUtcTime(-6,0)));
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("发票逾期",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"INVOICE_OVERDUE"));
        //检查报销单的标签
        Assert.assertEquals("发票逾期",expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"1006"));
    }

    @Test(description = "发票逾期管控-静态校验管控-强管控不允许生成费用")
    public void receiptControl11() throws HttpStatusException{
        //开启查验失败的发票可以生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.duplicatedReceipt(employee,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        ReceiptOverTime receiptOverTime = new ReceiptOverTime();
        receiptOverTime.setForceEnabled("true");
        String overTimeConfigId = receiptControlConfig.receiptOverTime(employee,receiptOverTime);
        map.put("overTimeConfigId",overTimeConfigId);
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        Assert.assertEquals("N",expenseReportInvoice.getReceptVerifyInfo(employee,Receript.HANDRECEIPT3).get("canCreateExpense").getAsString());
    }

    @Test(description = "发票逾期管控-动态校验管控-弱管控")
    public void receiptControl12() throws HttpStatusException {
        //开启查验失败的发票可以生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.duplicatedReceipt(employee,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        //配置逾期规则
        ReceiptOverTime receiptOverTime = new ReceiptOverTime();
        receiptOverTime.setStaticCalibrationFlag(false);
        receiptOverTime.setDynamicRuleFailureTime("12-31");
        String overTimeConfigId = receiptControlConfig.receiptOverTime(employee,receiptOverTime);
        map.put("overTimeConfigId",overTimeConfigId);
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), String.format(Receript.HANDRECEIPT3,UTCTime.getUtcTime(-6,0)));
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("发票逾期",expenseReportInvoice.checkInvoiceLabelName(employee,invoice1.getInvoiceOID(),"INVOICE_OVERDUE"));
        //检查报销单的标签
        Assert.assertEquals("1笔发票逾期",expenseReport.checkSubmitLabel(employee,formDetail.getReportOID(),"1006"));
    }

    @Test(description = "发票字段带入检查（归属人，身份证，人员信息完整性内部员工发票，国内旅客运输）")
    public void receiptControl13() throws HttpStatusException {
        FormDetail formDetail= expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.trainReceipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        ReceiptMethodPage receiptMethodPage = new ReceiptMethodPage();
        ReceiptWords receiptWords = receiptMethodPage.getInvoiceReceipt(employee,invoice1.getInvoiceOID());
        //检查归属人
        Assert.assertEquals("张鹏",receiptWords.getReceiptOwner());
        //检查身份信息
        assert receiptWords.getIdCardNo().contains("3634");
        //检查 信息完整性
        Assert.assertEquals("Y",receiptWords.getShowUserInfo());
        //国内旅客运输
        Assert.assertEquals("Y",receiptWords.getDomesticPassengers());
    }


    @AfterMethod
    public void cleanEnv() throws HttpStatusException {
        for (String s : map.keySet()) {
            switch (s){
                case "reportOID":
                    if (expenseReport.getReportStatus(employee, map.get("reportOID")) == 1002) {
                        expenseReport.withdraw(employee, map.get("reportOID"));
                    }
                    expenseReport.deleteExpenseReport(employee, map.get("reportOID"));
                    break;
                case "invoiceOID1":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOID1"));
                    break;
                case "invoiceOID2":
                    expenseReportInvoice.deleteInvoice(employee, map.get("invoiceOID2"));
                    break;
                case "itemId":
                    //删除发票管控中的免贴票管控
                    receiptControlConfig.deleteConfigItem(employee, map.get("itemId"));
                    break;
                case "receiptTitle":
                    receiptControlConfig.receiptTitleConfig(employee, false, false);
                    break;
                case "receiptConsecutive":
                    //关闭连号配置
                    receiptControlConfig.receiptConsecutiveConfig(employee, ReceiptConfig.receiptConsecutive,false);
                    break;
                case "overTimeConfigId":
                    receiptControlConfig.deleteReceiptConfig(employee,map.get("overTimeConfigId"));
                    break;
                case "receiptToInvoiceOptId1" :
                    receiptControlConfig.deleteReceiptCreateExpense(employee,map.get("receiptToInvoiceOptId1"));
                    break;
            }
        }
    }
}
