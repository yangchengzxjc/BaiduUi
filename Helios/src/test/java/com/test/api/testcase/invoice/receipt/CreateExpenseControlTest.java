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

    @Test(description = "发票重复可生成费用-弱管控")
    public void createExpenseControlTest01() throws HttpStatusException, ParseException {
        //配置规则 初始化
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        ReceiptCreateExpense receiptCreateExpense2 = new ReceiptCreateExpense();
        String invoiceNumber = RandomNumber.getTimeNumber(8);
        String checkCode = RandomNumber.getTimeNumber(6);
        String invoiceDate = UTCTime.getUtcTime(-4,0);
        //生成一个随机的发票
        String receipt = receiptControlConfig.getHandReceipt(invoiceNumber,checkCode,invoiceDate);
        //初始化发票重复的规则
        receiptCreateExpense1.setDuplicatedReceipt(receiptMethodPage.receiptCreateExpenseControl("Y"));
        receiptCreateExpense2.setCancelledReceipt(receiptMethodPage.receiptCreateExpenseControl("-"));
        String receiptToInvoiceOptId1 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"Y");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        String receiptToInvoiceOptId2 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense2,"Y");
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), receipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("Y",expenseReportInvoice.getReceptVerifyInfo(employee,receipt).get("canCreateExpense").getAsString());
        FormDetail invoice2 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), receipt);
        map.put("invoiceOID2",invoice2.getInvoiceOID());
//        // 检查报销单内的费用是否是俩个
        Assert.assertEquals(2,expenseReportInvoice.getInvoiceDetail(employee,formDetail.getReportOID()).size());
    }

    @Test(description = "发票重复可生成费用-强管控不可生成费用")
    public void createExpenseControlTest02() throws HttpStatusException, ParseException {
        //配置规则 初始化
        ReceiptCreateExpense receiptCreateExpense1 = new ReceiptCreateExpense();
        ReceiptCreateExpense receiptCreateExpense2 = new ReceiptCreateExpense();
        String invoiceNumber = RandomNumber.getTimeNumber(6);
        String checkCode = RandomNumber.getTimeNumber(6);
        String invoiceDate = UTCTime.getUtcTime(-4,0);
        String receipt = receiptControlConfig.getHandReceipt(invoiceNumber,checkCode,invoiceDate);
        receiptCreateExpense1.setDuplicatedReceipt(receiptMethodPage.receiptCreateExpenseControl("Y"));
        receiptCreateExpense2.setCancelledReceipt(receiptMethodPage.receiptCreateExpenseControl("-"));
        String receiptToInvoiceOptId1 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense1,"N");
        map.put("receiptToInvoiceOptId1",receiptToInvoiceOptId1);
        String receiptToInvoiceOptId2 = receiptControlConfig.receiptCreateExpense(employee,receiptCreateExpense2,"Y");
        map.put("receiptToInvoiceOptId2",receiptToInvoiceOptId2);
        FormDetail formDetail = expenseReportPage.setDailyReport(employee, UTCTime.getFormDateEnd(3),"自动化测试-日常报销单",new String[]{employee.getFullName()});
        map.put("reportOID",formDetail.getReportOID());
        FormDetail invoice1 = expenseReportPage.setHandReceiptInvoice(employee,"autotest",formDetail.getReportOID(), receipt);
        map.put("invoiceOID1",invoice1.getInvoiceOID());
        Assert.assertEquals("N",expenseReportInvoice.getReceptVerifyInfo(employee,receipt).get("canCreateExpense").getAsString());
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
            }
        }
    }
}
