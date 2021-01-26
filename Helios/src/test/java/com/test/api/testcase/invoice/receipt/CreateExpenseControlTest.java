package com.test.api.testcase.invoice.receipt;

import com.hand.baseMethod.HttpStatusException;
import com.hand.basicConstant.ReceiptConfig;
import com.hand.basicConstant.Receript;
import com.hand.basicObject.Employee;
import com.hand.basicObject.Rule.receiptConfig.ReceiptCreateExpense;
import com.hand.basicObject.component.FormDetail;
import com.hand.utils.RandomNumber;
import com.hand.utils.UTCTime;
import com.test.BaseTest;
import com.test.api.method.BusinessMethod.ExpenseReportPage;
import com.test.api.method.ExpenseMethod.ReceiptMethodPage;
import com.test.api.method.ExpenseReport;
import com.test.api.method.ExpenseReportInvoice;
import com.test.api.method.ReceiptControlConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.ParseException;
import java.util.HashMap;

/**
 * @Author peng.zhang
 * @Date 2021/1/7
 * @Version 1.0
 **/
@Slf4j
public class CreateExpenseControlTest extends BaseTest {

    private Employee employee;
    private ExpenseReportPage expenseReportPage;
    private ExpenseReport expenseReport;
    private ReceiptControlConfig receiptControlConfig;
    private ExpenseReportInvoice expenseReportInvoice;
    private HashMap<String,String> map;
    private ReceiptMethodPage receiptMethodPage;

    @BeforeClass
    @Parameters({"phoneNumber", "passWord", "environment"})
    public void beforeClass(@Optional("14082971222") String phoneNumber, @Optional("zp123456") String pwd, @Optional("stage") String env) {
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

    @Test(description = "发票重复可生成费用-弱管控")
    public void createExpenseControlTest01() throws HttpStatusException, ParseException {
        String invoiceNumber = RandomNumber.getTimeNumber(8);
        String checkCode = RandomNumber.getTimeNumber(6);
        String invoiceDate = UTCTime.getUtcTime(-4,0);
        //生成一个随机的发票
        String receipt = receiptControlConfig.getHandReceipt(invoiceNumber,checkCode,invoiceDate);
        //初始化发票重复的规则
        String receiptToInvoiceOptId1 = receiptMethodPage.duplicatedReceipt(employee,"Y");
        String receiptToInvoiceOptId2 = receiptMethodPage.cancelledReceipt(employee);
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), receipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("Y",expenseReportInvoice.getReceptVerifyInfo(employee,receipt).get("isOk").getAsString());
        FormDetail invoice2 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), receipt);
        map.put("invoiceOID2",invoice2.getInvoiceOID());
        // 检查报销单内的费用是否是俩个
        Assert.assertEquals(2,expenseReportInvoice.getInvoiceDetail(employee,formDetail.getReportOID()).size());
    }

    @Test(description = "发票重复可生成费用-强管控不可生成费用")
    public void createExpenseControlTest02() throws HttpStatusException, ParseException {
        //配置规则 初始化
        String invoiceNumber = RandomNumber.getTimeNumber(6);
        String checkCode = RandomNumber.getTimeNumber(6);
        String invoiceDate = UTCTime.getUtcTime(-4,0);
        String receipt = receiptControlConfig.getHandReceipt(invoiceNumber,checkCode,invoiceDate);
        String receiptToInvoiceOptId1 = receiptMethodPage.duplicatedReceipt(employee,"N");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        String receiptToInvoiceOptId2 = receiptMethodPage.cancelledReceipt(employee);
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), receipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("N",expenseReportInvoice.getReceptVerifyInfo(employee,receipt).get("isOk").getAsString());
    }

    @Test(description = "发票连号可生成费用-弱管控")
    public void createExpenseControlTest03() throws HttpStatusException{
        // 开启连号校验
        receiptControlConfig.receiptConsecutiveConfig(employee, ReceiptConfig.receiptConsecutive,true);
        map.put("receiptConsecutive","true");
        // 配置 发票连号可生成费用
        String receiptToInvoiceOptId2 = receiptMethodPage.duplicatedReceipt(employee,"Y");
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        // 配置发票查验失败可生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.cancelledReceipt(employee);
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //发票连号的费用
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.HANDRECEIPT5);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("Y",expenseReportInvoice.getReceptVerifyInfo(employee,Receript.HANDRECEIPT6).get("isOk").getAsString());
    }

    @Test(description = "发票连号可生成费用-强管控")
    public void createExpenseControlTest04() throws HttpStatusException{
        // 开启连号校验
        receiptControlConfig.receiptConsecutiveConfig(employee, ReceiptConfig.receiptConsecutive,true);
        map.put("receiptConsecutive","true");
        // 配置 发票连号可生成费用
        String receiptToInvoiceOptId2 = receiptMethodPage.consecutiveReceipt(employee,"N");
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        // 配置发票查验失败可生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.cancelledReceipt(employee);
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //发票连号的费用
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.HANDRECEIPT5);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        FormDetail invoice2 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.HANDRECEIPT6);
        Assert.assertEquals("该费用类型不可报销连号的发票",invoice2.getResponse().get("message").getAsString());
    }

    @Test(description = "发票抬头不一致生成费用-弱管控")
    public void createExpenseControlTest05() throws HttpStatusException{
        // 开启发票抬头管控
        receiptControlConfig.receiptTitleConfig(employee,true,true);
        map.put("receiptTitle","true");
        // 配置 配置发票抬头不一致可生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.invalidTitleReceipt(employee,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //发票抬头校验 发票抬头标签
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.receipt6);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals(1,expenseReportInvoice.getInvoiceDetail(employee,formDetail.getReportOID()).size());
    }

    @Test(description = "发票抬头不一致不可以生成费用-强管控")
    public void createExpenseControlTest06() throws HttpStatusException{
        // 开启发票抬头管控
        receiptControlConfig.receiptTitleConfig(employee,true,true);
        map.put("receiptTitle","true");
        // 配置 配置发票抬头不一致可生成费用
        String receiptToInvoiceOptId1 = receiptMethodPage.invalidTitleReceipt(employee,"N");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        //发票抬头校验 发票抬头标签
        String message = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(), Receript.receipt6).getResponse().get("message").getAsString();
        Assert.assertEquals("该费用类型不可报销抬头有误的发票",message);
    }

    @Test(description = "存在多个发票行-弱管控可生成费用(异税率待定测试)",enabled = false)
    public void createExpenseControlTest07() throws HttpStatusException{
        //配置规则 初始化
        ReceiptCreateExpense receiptCreateExpense2 = new ReceiptCreateExpense();
        //开启多发票行可生成费用
        receiptCreateExpense2.setMultipleInvoiceLines(receiptMethodPage.receiptCreateExpenseControl("Y"));
        String receiptToInvoiceOptId2 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense2,"Y");
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(),Receript.moreTaxRateReceipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals(1,expenseReportInvoice.getInvoiceDetail(employee,formDetail.getReportOID()).size());
    }

    @Test(description = "存在多个发票行-强管控不可生成费用(异税率待定测试)",enabled = false)
    public void createExpenseControlTest08() throws HttpStatusException{
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        //开启多发票行不可生成费用
        receiptCreateExpense1.setMultipleInvoiceLines(receiptMethodPage.receiptCreateExpenseControl("Y"));
        String receiptToInvoiceOptId1 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"N");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
//        Assert.assertEquals("N",expenseReportInvoice.getOCRReceiptVerifyInfo(employee,Receript.moreTaxRateReceipt).get("isOk").getAsString());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(),Receript.moreTaxRateReceipt);
        log.info("能否生成费用:{}",invoice1.getResponse());
    }

    @Test(description = "商品名称服务包含某些特殊名称的发票-可生成费用")
    public void createExpenseControlTest09() throws HttpStatusException{
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        //开启多发票行不可生成费用
        receiptCreateExpense1.setGoodsServiceName("牙膏");
        String receiptToInvoiceOptId1 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(),Receript.receipt7);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals(1,expenseReportInvoice.getInvoiceDetail(employee,formDetail.getReportOID()).size());
    }

    @Test(description = "商品名称服务包含某些特殊名称的发票-不可生成费用")
    public void createExpenseControlTest10() throws HttpStatusException{
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        //开启多发票行不可生成费用
        receiptCreateExpense1.setGoodsServiceName("牙膏");
        String receiptToInvoiceOptId1 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"N");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(),Receript.receipt7);
        Assert.assertEquals("该费用类型不可报销商品/服务名称包含“牙膏”的发票",invoice1.getResponse().get("message").getAsString());
    }

    @Test(description = "销售方名称包含某字段-不可生成费用")
    public void createExpenseControlTest11() throws HttpStatusException{
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        //开启多发票行不可生成费用
        receiptCreateExpense1.setSalesParty("西安华讯得");
        String receiptToInvoiceOptId1 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"N");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setReceiptInvoice(employee,"autotest",formDetail.getReportOID(),Receript.receipt7);
        Assert.assertEquals("该费用类型不可报销销售方名称包含“西安华讯得”的发票",invoice1.getResponse().get("message").getAsString());
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
                case "receiptToInvoiceOptId1" :
                    receiptControlConfig.deleteReceiptCreateExpense(employee,map.get("receiptToInvoiceOptId1"));
                    break;
                case "receiptToInvoiceOptId2" :
                    receiptControlConfig.deleteReceiptCreateExpense(employee,map.get("receiptToInvoiceOptId2"));
                    break;
                case "receiptConsecutive":
                    //关闭连号配置
                    receiptControlConfig.receiptConsecutiveConfig(employee, ReceiptConfig.receiptConsecutive,false);
                    break;
                case "receiptTitle":
                    receiptControlConfig.receiptTitleConfig(employee, false, false);
                    break;
            }
        }
    }
}
